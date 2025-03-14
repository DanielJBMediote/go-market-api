import { BadRequestException, Injectable, NotFoundException, UnauthorizedException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AuthContextService } from "src/auth/auth-context.service";
import { WhereBuilder, WhereParams } from "src/database/where-builder.class";
import { User } from "src/users/entities/user.entity";
import { Repository } from "typeorm";
import { CreateStoreDTO } from "./dto/create-store.dto";
import { UpdateStoreDTO } from "./dto/update-store.dto";
import { Store } from "./entities/store.entity";

@Injectable()
export class StoresService {
  constructor(
    @InjectRepository(Store)
    private storeRepository: Repository<Store>,
    @InjectRepository(User)
    private userRepository: Repository<User>,
    private authContextService: AuthContextService
  ) {}

  async create(createStoreDTO: CreateStoreDTO) {
    const currentUser = this.authContextService.getCurrentUser();
    const ownerId = Number(currentUser.sub);

    const { name, description, slug } = createStoreDTO;

    if (!ownerId) throw new BadRequestException("OwnerId must be provided.");

    const existStoreByName = await this.storeRepository.findOneBy({ name });
    if (existStoreByName) {
      throw new BadRequestException("Storage already exists with this name.");
    }

    const owner = await this.userRepository.findOneBy({ id: Number(ownerId) });
    if (!owner) throw new BadRequestException("User does not exists.");

    const createdStore = this.storeRepository.create({
      name,
      description,
      slug,
      owner: { id: ownerId },
    });

    return this.storeRepository.save(createdStore);
  }

  async findAll(whereParams?: WhereParams) {
    const where = new WhereBuilder()
      .addCondition(whereParams?.column, whereParams?.value, whereParams?.operator)
      .build();

    return await this.storeRepository.find({
      where,
      relations: {
        owner: true,
      },
      select: {
        id: true,
        name: true,
        description: true,
        slug: true,
        createdAt: true,
        owner: {
          id: true,
          name: true,
          email: true,
        },
      },
    });
  }

  findOne(id: number) {
    return this.storeRepository.findOneBy({ id });
  }

  async update(id: number, updateStoreDTO: UpdateStoreDTO) {
    const currentUser = this.authContextService.getCurrentUser();

    // Busca a loja junto com o dono em uma única consulta
    const foundStore = await this.storeRepository.findOne({
      where: { id },
      relations: ["owner"],
    });

    if (!foundStore) {
      throw new NotFoundException("Store not found.");
    }

    // Verifica se o usuário logado é o dono
    if (foundStore.owner.id !== Number(currentUser.sub)) {
      throw new UnauthorizedException("Can't update store of another owner.");
    }

    // Verifica se o nome já está em uso por outra loja
    const existingStore = await this.storeRepository.findOne({
      where: { name: updateStoreDTO.name },
    });

    if (existingStore && existingStore.id !== id) {
      throw new BadRequestException("Store with this name already exists.");
    }

    await this.storeRepository.update(id, updateStoreDTO);
    return this.storeRepository.findOne({ where: { id } }); // Retorna a loja atualizada
  }

  remove(id: number) {
    return this.storeRepository.delete(id);
  }
}
