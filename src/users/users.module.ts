import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { HashService } from "src/auth/hash.service";
import { DatabaseModule } from "src/database/database.module";
import { User } from "./entities/user.entity";
import { Users } from "./users";
import { UsersController } from "./users.controller";
import { UsersService } from "./users.service";

@Module({
  imports: [TypeOrmModule.forFeature([User]), DatabaseModule],
  controllers: [UsersController],
  providers: [UsersService, Users, HashService],
  exports: [TypeOrmModule],
})
export class UsersModule {}
