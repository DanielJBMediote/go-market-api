import {
  BadRequestException,
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Patch,
  Post,
  Query,
  Req,
  UseGuards,
} from "@nestjs/common";
import { Request } from "express";
import { JwtAuthGuard } from "src/auth/jwt-auth.guard";
import { WhereParams } from "src/database/where-builder.class";
import { CreateStoreDTO } from "./dto/create-store.dto";
import { UpdateStoreDTO } from "./dto/update-store.dto";
import { StoresService } from "./stores.service";

@UseGuards(JwtAuthGuard)
@Controller("stores")
export class StoresController {
  constructor(
    private readonly storesService: StoresService,
    private jwtAuthGuard: JwtAuthGuard
  ) {}

  @Post()
  create(@Body() createStoreDTO: CreateStoreDTO, @Req() request: Request) {
    const jwtToken = this.jwtAuthGuard.extractTokenFromHeader(request);
    if (!jwtToken) throw new BadRequestException();

    const userId = this.jwtAuthGuard.getUSerFromJwt(jwtToken);
    return this.storesService.create({
      ...createStoreDTO,
      ownerId: Number(userId),
    });
  }

  @Get()
  findAll(@Query() where: WhereParams) {
    return this.storesService.findAll(where);
  }

  @Get(":id")
  findOne(@Param("id") id: string) {
    return this.storesService.findOne(+id);
  }

  @Patch(":id")
  update(@Param("id") id: string, @Body() updateStoreDto: UpdateStoreDTO) {
    return this.storesService.update(+id, updateStoreDto);
  }

  @Delete(":id")
  remove(@Param("id") id: string) {
    return this.storesService.remove(+id);
  }
}
