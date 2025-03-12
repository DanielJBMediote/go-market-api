import { Module } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import { UsersModule } from "src/users/users.module";
import { UsersService } from "src/users/users.service";
import { AuthService } from "./auth.service";
import { HashService } from "./hash.service";

@Module({
  imports: [UsersModule],
  providers: [AuthService, HashService, UsersService, JwtService],
  exports: [AuthService, HashService],
})
export class AuthModule {}
