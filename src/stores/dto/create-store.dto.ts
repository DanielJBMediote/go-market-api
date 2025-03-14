import { IsNotEmpty } from "class-validator";

export class CreateStoreDTO {
  @IsNotEmpty({ message: "Name cannot be empty" })
  name: string;

  slug: string;

  @IsNotEmpty({ message: "Description cannot be empty" })
  description: string;
}
