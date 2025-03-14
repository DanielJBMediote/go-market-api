import { Body, Controller, Delete, Get, Param, Patch, Post, Query, UseGuards } from "@nestjs/common";
import { JwtAuthGuard } from "src/auth/jwt-auth.guard";
import { WhereParams } from "src/database/where-builder.class";
import { CreateStoreDTO } from "./dto/create-store.dto";
import { UpdateStoreDTO } from "./dto/update-store.dto";
import { StoresService } from "./stores.service";

@UseGuards(JwtAuthGuard)
@Controller("stores")
export class StoresController {
  constructor(private readonly storesService: StoresService) {}

  @Post()
  create(@Body() createStoreDTO: CreateStoreDTO) {
    return this.storesService.create(createStoreDTO);
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
