import { Body, Controller, Get } from "@nestjs/common";
import { AuthService } from "./auth.service";

@Controller("auth")
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Get()
  async auth(
    @Body() { username, password }: { username: string; password: string }
  ) {
    return this.authService.login({ username, password });
  }
}
