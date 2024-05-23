import { Injectable } from '@angular/core';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { Router } from '@angular/router';
import { EncryptDecryptService } from './encrypt-decrypt.service';
import { UserMatrix } from '@core/models/user-matrix';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
  private userProfile: UserProfile;
  private webInfo: WebInfo;
  private userMatrix: UserMatrix;
  private jwt: string;

  constructor(private router: Router, private encryptDecryptService: EncryptDecryptService) {}

  public getUserProfile() {
    if (this.userProfile) {
      return this.userProfile;
    } else {
      if (!sessionStorage.getItem('userProfile')) {
      } else {
        const encrypted = JSON.parse(sessionStorage.getItem('userProfile'));
        const decryp = this.encryptDecryptService.decrypt(encrypted);
        this.userProfile = JSON.parse(decryp);
      }

      return this.userProfile;
    }
  }

  public setUserProfile(userProfile: UserProfile) {
    userProfile.DATELOGIN = new Date();
    const encryptedText = this.encryptDecryptService.encrypt(JSON.stringify(userProfile));
    sessionStorage.setItem('userProfile', JSON.stringify(encryptedText));
  }

  public getWebInfo() {
    if (this.webInfo) {
      return this.webInfo;
    } else {
      if (!sessionStorage.getItem('webInfo')) {
        this.router.navigate(['/']);
      } else {
        const encrypted = JSON.parse(sessionStorage.getItem('webInfo'));
        const decryp = this.encryptDecryptService.decrypt(encrypted);
        this.webInfo = JSON.parse(decryp);
      }

      return this.webInfo;
    }
  }

  public setWebInfo(webInfo: WebInfo) {
    const encryptedText = this.encryptDecryptService.encrypt(JSON.stringify(webInfo));
    sessionStorage.setItem('webInfo', JSON.stringify(encryptedText));
  }

  public getUserMatrix(): UserMatrix {
    if (this.userMatrix) {
      return this.userMatrix;
    } else {
      if (!sessionStorage.getItem('userMatrix')) {
        this.router.navigate(['/']);
      } else {
        const encrypted = JSON.parse(sessionStorage.getItem('userMatrix'));
        const decryp = this.encryptDecryptService.decrypt(encrypted);
        this.userMatrix = JSON.parse(decryp);
      }

      return this.userMatrix;
    }
  }

  public setUserMatrix(userMatrix: UserMatrix) {
    const encryptedText = this.encryptDecryptService.encrypt(JSON.stringify(userMatrix));
    sessionStorage.setItem('userMatrix', JSON.stringify(encryptedText));
  }

  public setToken(accessToken: string, jwt: string) {
    const encryptedTextAccessToken = this.encryptDecryptService.encrypt(accessToken);
    const encryptedTextJWT = this.encryptDecryptService.encrypt(jwt);
    sessionStorage.setItem('accessToken', encryptedTextAccessToken);
    sessionStorage.setItem('jwt', encryptedTextJWT);
  }

  public getJWT(): string {
    if (this.jwt) {
      return this.jwt;
    } else {
      if (!sessionStorage.getItem('jwt')) {
        // this.router.navigate(['/']);
      } else {
        const encrypted = sessionStorage.getItem('jwt');
        const decryp = this.encryptDecryptService.decrypt(encrypted);
        this.jwt = decryp;
      }

      return this.jwt;
    }
  }
}
