import { Injectable } from "@nestjs/common";
import * as bcrypt from "bcryptjs";

@Injectable()
export class HashService {
  async hashPassword(password: string): Promise<string> {
    return await bcrypt.hash(password, 8);
  }

  async verifyPassword(hash: string, password: string): Promise<boolean> {
    return await bcrypt.compare(hash, password);
  }
}
