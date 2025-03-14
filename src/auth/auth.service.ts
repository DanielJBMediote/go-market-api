import {
  Injectable,
  NotAcceptableException,
  NotFoundException,
} from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import { JwtService } from "@nestjs/jwt";
import { UsersService } from "src/users/users.service";
import { HashService } from "./hash.service";

@Injectable()
export class AuthService {
  constructor(
    private readonly configService: ConfigService,
    private readonly usersService: UsersService,
    private readonly jwtService: JwtService,
    private readonly hashService: HashService,
  ) {}

  async signIn({ username, password }: { username: string; password: string }) {
    const user = await this.usersService.findByUsernameOrEmail(username);

    if (!user) {
      throw new NotFoundException("Nenhum usuário encontrado!");
    }

    const isPasswordValid = await this.hashService.verifyPassword(
      password,
      user.password,
    );

    if (!isPasswordValid) {
      throw new NotAcceptableException("Username e/ou senha incorretos!");
    }

    // Gerar um token JWT
    const payload = { sub: user.id, username: user.username };
    const token = this.jwtService.sign(payload, {
      expiresIn: "2h",
      secret: this.configService.get<string>("JWT_SECRET"),
    });

    return {
      access_token: token,
      user: {
        id: user.id,
        name: user.name,
        username: user.username,
        email: user.email,
        role: user.role,
      },
    };
  }
}
