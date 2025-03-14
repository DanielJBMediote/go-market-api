import { IsNotEmpty, MinLength } from "class-validator";
import { Role } from "src/enums/roles";

export class CreateUserDTO {
  @IsNotEmpty()
  name: string;

  @IsNotEmpty()
  username: string;

  @IsNotEmpty()
  email: string;

  @IsNotEmpty()
  @MinLength(6)
  password: string;

  role: Role;
}
