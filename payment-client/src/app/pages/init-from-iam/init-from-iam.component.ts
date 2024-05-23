import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { ActivatedRoute, Router } from '@angular/router';
import { WebInfoService } from '@core/services/web-info.service';
import { SidebarService } from '@core/services/sidebar.service';
import { MatSnackBar } from '@angular/material';
import { UserMatrix } from '@core/models/user-matrix';
import { EncryptDecryptService, LocalStorageService, MasterService } from '@core/services';
import { IamService } from '@core/services/authorize/iam/iam.service';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { environment } from '@env/environment';

@Component({
  selector: 'app-init-from-iam',
  templateUrl: './init-from-iam.component.html',
  styleUrls: ['./init-from-iam.component.scss'],
})
export class InitFromIamComponent implements OnInit, OnDestroy, AfterViewInit {
  authCode = '';
  userProfile: UserProfile;
  private _destroyed$ = new Subject();
  private webInfo: WebInfo;

  constructor(
    private router: Router,
    private localStorageService: LocalStorageService,
    private masterService: MasterService,
    private webInfoService: WebInfoService,
    private sidebarService: SidebarService,
    private encryptDecryptService: EncryptDecryptService,
    private route: ActivatedRoute,
    private iamService: IamService,
    private snackBar: MatSnackBar,
    private utils: Utils,
    private constant: Constant
  ) {}

  ngOnInit() {
    window.scrollTo(0, 0);
    this.route.queryParams.subscribe((params) => {
      console.log(params);

      console.log('aaa');
      if (params.auth_code) {
        this.authCode = params.auth_code;
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.authCode) {
      this.getUserProfile(this.authCode);
    } else {
      window.location.href = `${environment.portal}`;
    }
  }

  ngOnDestroy(): void {
    this._destroyed$.next();
    this._destroyed$.complete();
  }

  getUserProfile(authCode) {
    this.iamService.getToken(authCode).then((response) => {
      console.log(response);
      if (response.responseCode === '00') {
        this.localStorageService.setToken(
          response.responseContent.accessToken,
          response.responseContent.jwt
        );

        this.iamService
          .getUserprofile(response.responseContent.accessToken)
          .then((res) => {
            if (res.responseCode === '00') {
              // console.log('res', res.responseContent);

              //
              // console.log(JSON.stringify(res.responseContent));

              const defaultWebInfo: WebInfo = {
                fiArea: res.responseContent.areaCode,
                compCode: res.responseContent.departmentCode,
                ipAddress: '',
                paymentCenter: res.responseContent.divisionCode,
                userWebOnline: res.responseContent.username,
                defaultFiArea: res.responseContent.areaCode,
                defaultCompCode: res.responseContent.departmentCode,
                defaultPaymentCenter: res.responseContent.divisionCode,
                authCompanyCode: [],
                authCostCenter: [],
                authFIArea: [],
                authPaymentCenter: [],
              };
              this.localStorageService.setWebInfo(defaultWebInfo);

              const result = {} as UserProfile;
              result.userdata = res.responseContent;
              this.localStorageService.setUserProfile(result);
              this.sidebarService.updateUserProfile(result);
            }
          })
          .then(() => {
            const userProfile = this.localStorageService.getUserProfile();
            // tslint:disable-next-line:no-shadowed-variable
            this.iamService
              .getUserMatrix(response.responseContent.accessToken, userProfile.userdata.username)
              .then((response) => {
                console.log('userMatrix');
                console.log(JSON.stringify(response));
                const userMatrix = response.responseContent as UserMatrix;
                console.log(userMatrix);
                this.localStorageService.setUserMatrix(userMatrix);
                this.router.navigate(['/menu']);
              })
              .then(() => {
                this.searchPaymentMethod();
                this.searchDocType();
                this.searchSpecialGL();
              });
          });
      }
    });
  }

  async searchPaymentMethod() {
    await this.masterService.findPaymentMethodCodeWithParam('**').then((value) => {
      this.constant.PAYMENT_METHOD = value.data;
      this.constant.PAYMENT_METHOD.forEach((item) => {
        this.constant.PAYMENT_METHOD_SEARCH.push(item.valueCode);
      });
    });
  }

  async searchSpecialGL() {
    await this.masterService.findSpecialGLCodeWithParam('**').then((value) => {
      this.constant.PAYMENT_SPECIAL_GL = value.data;
      this.constant.PAYMENT_SPECIAL_GL.forEach((item) => {
        this.constant.PAYMENT_SPECIAL_GL_SEARCH.push(item.valueCode);
      });
    });
  }

  async searchDocType() {
    await this.masterService.findDocTypeWithParam('**').then((value) => {
      this.constant.DOC_TYPE = value.data;
      this.constant.DOC_TYPE.forEach((item) => {
        this.constant.DOC_TYPE_SEARCH.push(item.valueCode);
      });
    });
  }
}
