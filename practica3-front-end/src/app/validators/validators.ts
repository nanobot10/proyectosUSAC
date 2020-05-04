import { AbstractControl, ValidatorFn } from '@angular/forms';

export function validateAmount(balance: number): ValidatorFn {
    return (control: AbstractControl) => {
        if (balance < control.value) {
            return { validAmount: true };
        }
        return null;
    };
}
