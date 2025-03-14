import { BadRequestException, Inject, Injectable, Scope } from "@nestjs/common";
import { REQUEST } from "@nestjs/core";
import { Request } from "express";
import { JwtAuthGuard } from "./jwt-auth.guard";

@Injectable({ scope: Scope.REQUEST })
export class AuthContextService {
  constructor(
    @Inject(REQUEST) private readonly request: Request,
    private readonly jwtAuthGuard: JwtAuthGuard
  ) {}

  getCurrentUser() {
    const token = this.jwtAuthGuard.extractTokenFromHeader(this.request);
    if (!token) {
      throw new BadRequestException("Token not found.")
    }
    try {
      return this.jwtAuthGuard.getUSerFromJwt(token);
    } catch {
      throw new BadRequestException("User Context not found.")
    }
  }
}
