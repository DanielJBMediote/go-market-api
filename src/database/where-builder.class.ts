import {
  LessThan,
  LessThanOrEqual,
  Like,
  MoreThan,
  MoreThanOrEqual,
} from "typeorm";

type OperatorType =
  | "LIKE"
  | "EQUALS"
  | "GREATER_THAN"
  | "GREATER_THAN_OR_EQUALS"
  | "LESS_THAN"
  | "LESS_THAN_OR_EQUALS"
  | "BEFORE_DATE"
  | "AFTER_DATE";

export interface WhereParams {
  value: string;
  column: string;
  operator: OperatorType;
}

export class WhereBuilder {
  private where: Record<string, any> = {};

  addCondition(
    column?: string,
    value?: string | Date,
    operator: OperatorType = "EQUALS"
  ) {
    if (!column || value === undefined || value === null) return this;

    switch (operator) {
      case "LIKE":
        this.where[column] = Like(`%${value}%`);
        break;
      case "GREATER_THAN":
        this.where[column] = MoreThan(value);
        break;
      case "GREATER_THAN_OR_EQUALS":
        this.where[column] = MoreThanOrEqual(value);
        break;
      case "LESS_THAN":
        this.where[column] = LessThan(value);
        break;
      case "LESS_THAN_OR_EQUALS":
        this.where[column] = LessThanOrEqual(value);
        break;
      case "BEFORE_DATE":
        this.where[column] = LessThan(new Date(value as string));
        break;
      case "AFTER_DATE":
        this.where[column] = MoreThan(new Date(value as string));
        break;
      default:
        this.where[column] = value;
    }

    return this;
  }

  build() {
    return this.where;
  }
}
