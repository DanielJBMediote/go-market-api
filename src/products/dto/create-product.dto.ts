import { IsNotEmpty, Min } from "class-validator";

export class CreateProductDTO {
  @IsNotEmpty()
  name: string;

  @IsNotEmpty()
  description: string;

  @IsNotEmpty()
  @Min(0)
  price: number;

  @IsNotEmpty()
  storeId: number;

  imageUrl: string;
}
