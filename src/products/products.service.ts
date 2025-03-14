import { BadRequestException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { WhereBuilder, WhereParams } from "src/database/where-builder.class";
import { Repository } from "typeorm";
import { CreateProductDTO } from "./dto/create-product.dto";
import { UpdateProductDto } from "./dto/update-product.dto";
import { Product } from "./entities/product.entity";

@Injectable()
export class ProductsService {
  constructor(
    @InjectRepository(Product)
    private readonly productsRepository: Repository<Product>
  ) {}

  async create(createProductDTO: CreateProductDTO) {
    const { price, name, description, storeId, imageUrl } = createProductDTO;

    if (price < 0) {
      throw new BadRequestException("Price cannot be negative");
    }

    const createdProduct = this.productsRepository.create({
      name,
      price,
      description,
      store: { id: storeId },
      imageUrl,
    });
    await this.productsRepository.save(createdProduct);

    return createdProduct;
  }

  async findAll(whereParams?: WhereParams) {
    const where = new WhereBuilder()
      .addCondition(
        whereParams?.column,
        whereParams?.value,
        whereParams?.operator
      )
      .build();
    return await this.productsRepository.find({ where });
  }

  findOne(id: number) {
    return this.productsRepository.findOneBy({ id });
  }

  async update(id: number, updateProductDTO: UpdateProductDto) {
    const foundProduct = this.productsRepository.findOneBy({ id });

    if (!foundProduct) {
      throw new BadRequestException("Product not found");
    }

    return await this.productsRepository.update(id, updateProductDTO);
  }

  remove(id: number) {
    return this.productsRepository.delete(id);
  }
}
