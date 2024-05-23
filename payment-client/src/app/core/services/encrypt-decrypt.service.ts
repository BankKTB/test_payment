import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';

@Injectable({
  providedIn: 'root',
})
export class EncryptDecryptService {
  secretKey = 'SecretKeyForEncryption';

  constructor() {}

  public encrypt(value: string): string {
    // console.log('value',value)
    return CryptoJS.AES.encrypt(value, this.secretKey.trim()).toString();
  }

  public decrypt(textToDecrypt: string) {
    return CryptoJS.AES.decrypt(textToDecrypt, this.secretKey.trim()).toString(CryptoJS.enc.Utf8);
  }
}
