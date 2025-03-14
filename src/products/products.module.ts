import { Module } from "@nestjs/common";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { JwtModule } from "@nestjs/jwt";
import { TypeOrmModule } from "@nestjs/typeorm";
import { AuthContextService } from "src/auth/auth-context.service";
import { AuthModule } from "src/auth/auth.module";
import { JwtAuthGuard } from "src/auth/jwt-auth.guard";
import { Store } from "src/stores/entities/store.entity";
import { Product } from "./entities/product.entity";
import { ProductsController } from "./products.controller";
import { ProductsService } from "./products.service";

@Module({
  imports: [
    TypeOrmModule.forFeature([Product, Store]),
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
  controllers: [ProductsController],
  providers: [ProductsService, AuthContextService, JwtAuthGuard],
  exports: [TypeOrmModule],
})
export class ProductsModule {}
