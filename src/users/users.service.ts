import { BadRequestException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { HashService } from "src/auth/hash.service";
import { Repository } from "typeorm";
import { CreateUserDTO } from "./dto/create-user.dto";
import { UpdateUserDto } from "./dto/update-user.dto";
import { User } from "./entities/user.entity";

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
    private readonly hashService: HashService
  ) {}

  async create(createUserDto: CreateUserDTO) {
    const usernameInUse = await this.usersRepository.existsBy({
      username: createUserDto.username,
    });
    const emailInUse = await this.usersRepository.existsBy({
      email: createUserDto.email,
    });

    if (emailInUse || usernameInUse) {
      throw new BadRequestException("Email e/ou Username já cadastrado!");
    }

    const password = await this.hashService.hashPassword(
      createUserDto.password
    );

    const createdUser = this.usersRepository.create({
      ...createUserDto,
      password,
    });
    await this.usersRepository.save(createdUser);

    const { password: _, ...useWithoutPassword } = createdUser;
    return useWithoutPassword;
  }

  findAll() {
    return this.usersRepository.find();
  }

  findOne(userId: number) {
    return this.usersRepository.findOneBy({ id: userId });
  }

  async findByUsernameOrEmail(identifier: string) {
    return await this.usersRepository.findOne({
      where: [{ username: identifier }, { email: identifier }],
    });
  }

  async update(id: number, updateUserDto: UpdateUserDto) {
    const existingUser = await this.usersRepository.findOneBy({ id });

    if (!existingUser) {
      throw new BadRequestException("Usuário não encontrado!");
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
      throw new BadRequestException("Email e/ou Username já cadastrado!");
    }

    await this.usersRepository.update(id, updateUserDto);
    return this.usersRepository.findOneBy({ id });
  }

  async remove(id: number) {
    const existingUser = await this.usersRepository.findOneBy({ id });

    if (!existingUser) {
      throw new Error("Não há Usuário com este ID");
    }

    await this.usersRepository.delete(id);
  }
}
