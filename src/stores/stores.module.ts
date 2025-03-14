import { Module } from "@nestjs/common";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { JwtModule } from "@nestjs/jwt";
import { TypeOrmModule } from "@nestjs/typeorm";
import { AuthContextService } from "src/auth/auth-context.service";
import { AuthModule } from "src/auth/auth.module";
import { JwtAuthGuard } from "src/auth/jwt-auth.guard";
import { User } from "src/users/entities/user.entity";
import { Store } from "./entities/store.entity";
import { StoresController } from "./stores.controller";
import { StoresService } from "./stores.service";

@Module({
  imports: [
    TypeOrmModule.forFeature([Store, User]),
    AuthModule,
    ConfigModule,
    JwtModule.registerAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        secret: configService.get<string>("JWT_SECRET"),
        signOptions: { expiresIn: "2h" },
      }),
      inject: [ConfigService],
    }),
  ],
  controllers: [StoresController],
  providers: [StoresService, AuthContextService, JwtAuthGuard],
  exports: [TypeOrmModule],
})
export class StoresModule {}
