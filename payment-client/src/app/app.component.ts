import { Component, OnInit } from '@angular/core';
import { LocalStorageService, MasterService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'payment-client';
  public isShowMenu = true;
  listPaymentCenters = [];
  listCostCenters = [];
  authPaymentCenter = [];
  authCostCenter = [];
  authFIArea = [];
  authCompanyCode = [];

  // constructor(private localStorageService: LocalStorageService, private utils: Utils) {
  //
  // }

  constructor(
    private localStorageService: LocalStorageService,
    private utils: Utils,
    private masterService: MasterService,
    private constant: Constant
  ) {
    console.log(this.localStorageService.getJWT());
    if (this.localStorageService.getJWT()) {
      this.searchPaymentMethod()
        .then(() => {
          this.searchDocType();
        })
        .then(() => {
          this.searchSpecialGL();
        });
    }
  }

  async searchPaymentMethod() {
    await this.masterService.findPaymentMethodCodeWithParam('**').then((value) => {
      this.constant.PAYMENT_METHOD = value.data;
      this.constant.PAYMENT_METHOD.forEach((item) => {
        this.constant.PAYMENT_METHOD_SEARCH.push(item.valueCode);
      });
    });
  }

  searchSpecialGL() {
    this.masterService.findSpecialGLCodeWithParam('**').then((value) => {
      this.constant.PAYMENT_SPECIAL_GL = value.data;
      this.constant.PAYMENT_SPECIAL_GL.forEach((item) => {
        this.constant.PAYMENT_SPECIAL_GL_SEARCH.push(item.valueCode);
      });
    });
  }

  searchDocType() {
    this.masterService.findDocTypeWithParam('**').then((value) => {
      const result = value.data.reduce((unique, o) => {
        if (!unique.some(obj => obj.name === o.name)) {
          unique.push(o);
        }
        return unique;
      }, []);

      this.constant.DOC_TYPE = result;

      this.constant.DOC_TYPE.forEach((item) => {
        this.constant.DOC_TYPE_SEARCH.push(item.valueCode);
      });
    });
  }

  ngOnInit() {}
}
