import { Injectable, NotFoundException, UnauthorizedException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { AuthContextService } from "src/auth/auth-context.service";
import { WhereBuilder, WhereParams } from "src/database/where-builder.class";
import { Store } from "src/stores/entities/store.entity";
import { Repository } from "typeorm";
import { CreateProductDTO } from "./dto/create-product.dto";
import { UpdateProductDto } from "./dto/update-product.dto";
import { Product } from "./entities/product.entity";

@Injectable()
export class ProductsService {
  constructor(
    @InjectRepository(Product)
    private productsRepository: Repository<Product>,
    private authContextService: AuthContextService,
    @InjectRepository(Store)
    private storeRepository: Repository<Store>
  ) {}

  async create(createProductDTO: CreateProductDTO) {
    const currentUser = this.authContextService.getCurrentUser();

    const { price, name, description, storeId, imageUrl } = createProductDTO;

    const store = await this.storeRepository.findOne({
      where: { id: storeId },
      relations: ["owner"],
      select: {
        id: true,
        name: true,
        description: true,
        owner: {
          id: true,
        },
      },
    });

    if (!store) {
      throw new NotFoundException("Store not found");
    }
    if (store.owner.id !== Number(currentUser.sub)) {
      throw new UnauthorizedException("Can't create a Product in Store of other owner!");
    }

    const createdProduct = this.productsRepository.create({
      name,
      price,
      description,
      store,
      imageUrl,
    });
    await this.productsRepository.save(createdProduct);

    return createdProduct;
  }

  async findAll(whereParams?: WhereParams) {
    const where = new WhereBuilder()
      .addCondition(whereParams?.column, whereParams?.value, whereParams?.operator)
      .build();
    return await this.productsRepository.find({ where });
  }

  findOne(id: number) {
    return this.productsRepository.findOneBy({ id });
  }

  async update(id: number, updateProductDTO: UpdateProductDto) {
    const currentUser = this.authContextService.getCurrentUser();

    const foundProduct = await this.productsRepository.findOne({
      where: { id },
      relations: ["store"],
    });

    if (!foundProduct) {
      throw new NotFoundException("Product not found");
    }

    const store = await this.storeRepository.findOne({ where: { id: foundProduct.store.id }, relations: ["owner"] });

    if (store && store.owner.id !== Number(currentUser.sub)) {
      throw new UnauthorizedException("Can't update product of another store owner.");
    }

    await this.productsRepository.update(id, updateProductDTO);

    return await this.productsRepository.findOneBy({ id });
  }

  remove(id: number) {
    return this.productsRepository.delete(id);
  }
}
