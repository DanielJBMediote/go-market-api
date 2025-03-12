import { IsNotEmpty, IsUUID, MinLength } from "class-validator";

export class CreateUserDTO {
  @IsUUID()
  id: number;

  @IsNotEmpty({ message: "Username cannot be empty" })
  username: string;

  @IsNotEmpty({ message: "Password cannot be empty" })
  @MinLength(6, { message: "Password must have 6 or more character" })
  password: string;

  @IsNotEmpty({ message: "Name cannot be empty" })
  name: string;

  @IsNotEmpty({ message: "E-mail cannot be empty" })
  email: string;
}
