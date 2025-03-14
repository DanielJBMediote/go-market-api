import {
  BadRequestException,
  Injectable,
  NotFoundException,
} from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { HashService } from "src/auth/hash.service";
import { Repository } from "typeorm";
import { CreateUserDTO } from "./dto/create-user.dto";
import { UpdateUserDTO } from "./dto/update-user.dto";
import { User } from "./entities/user.entity";

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
    private readonly hashService: HashService,
  ) {}

  async create(createUserDTO: CreateUserDTO) {
    const usernameInUse = await this.usersRepository.existsBy({
      username: createUserDTO.username,
    });
    const emailInUse = await this.usersRepository.existsBy({
      email: createUserDTO.email,
    });

    if (emailInUse || usernameInUse) {
      throw new BadRequestException("Email/Username already exists");
    }

    const hashedPassword = await this.hashService.hashPassword(
      createUserDTO.password,
    );

    const { name, email, role, username } = createUserDTO;

    const createdUser = this.usersRepository.create({
      name,
      email,
      role,
      username,
      password: hashedPassword,
    });
    await this.usersRepository.save(createdUser);

    const useWithoutPassword = this.getUserWithoutPassword(createdUser);
    return useWithoutPassword;
  }

  findAll() {
    return this.usersRepository.find();
  }

  findOne(userId: number) {
    return this.usersRepository.findOneBy({ id: userId });
  }

  async findByUsernameOrEmail(identifier: string) {
    const user = await this.usersRepository.findOne({
      where: [{ username: identifier }, { email: identifier }],
    });

    if (!user) throw new NotFoundException("Nenhum usuário encontrado!");

    return user;
  }

  async update(id: number, updateUserDto: UpdateUserDTO) {
    const existingUser = await this.usersRepository.findOneBy({ id });

    if (!existingUser) {
      throw new NotFoundException("User does not exists!");
    }

    const usernameInUse = await this.usersRepository.findOneBy({
      username: updateUserDto.username,
    });
    const emailInUse = await this.usersRepository.findOneBy({
      email: updateUserDto.email,
    });

    if (
      (emailInUse && emailInUse.id !== id) ||
      (usernameInUse && usernameInUse.id !== id)
    ) {
      throw new BadRequestException("Email/Username has already exists!");
    }

    await this.usersRepository.update(id, updateUserDto);

    const user = await this.usersRepository.findOneBy({ id });
    if (!user) {
      throw new NotFoundException("User not found after update");
    }

    const useWithoutPassword = this.getUserWithoutPassword(user);
    return useWithoutPassword;
  }

  async remove(id: number) {
    const existingUser = await this.usersRepository.findOneBy({ id });

    if (!existingUser) {
      throw new NotFoundException("User not found");
    }

    await this.usersRepository.delete(id);
  }

  private getUserWithoutPassword(user: User) {
    const { password: _, ...useWithoutPassword } = user;
    return useWithoutPassword;
  }
}
