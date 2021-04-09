export class CustomerInfo {
    name!: string;
    phone!: string;
    country!: string;
    valid!: boolean;

    constructor(name: string, phone: string, country: string, valid: boolean) {
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.valid = valid;
    }
}