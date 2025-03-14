import { IsEmpty, IsNotEmpty } from "class-validator";

export class CreateStoreDTO {
  @IsNotEmpty({ message: "Name cannot be empty" })
  name: string;

  @IsEmpty()
  slug: string;

  @IsNotEmpty({ message: "Description cannot be empty" })
  description: string;

  @IsNotEmpty({ message: "Stora must have an Owner" })
  ownerId: number;
}
