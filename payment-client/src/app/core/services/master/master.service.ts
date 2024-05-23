import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class MasterService {
  constructor(private httpClient: HttpClient) {
  }

  // รหัส Company
  findCompanyCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/compCode/getAll';
    } else {
      url = '/master/compCode/getByValue/' + textSearch;
    }
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส AreaCode
  findAreaCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/area/getAll';
    } else {
      url = '/master/area/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // ประเภทเอกสาร
  findDocTypeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/docType/getAll';
    } else {
      url = '/master/docType/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส specialGL
  findSpecialGLCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/specialGL/getAll';
    } else {
      url = '/master/specialGL/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส PaymentCenter
  findPaymentCenterCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/paymentCenter/getAll';
    } else {
      url = '/master/paymentCenter/getByValue/' + textSearch;
    }
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส Vendor
  findVendorCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/vendor/getAll';
    } else {
      url = '/master/vendor/getByValue/' + textSearch;
    }
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส Vendor
  findVendorBankAccountWithParam(textSearch, alternativeVendor, paymentMethodType): Promise<any> {
    const url =
      '/master/mmVendorBankAccount/getByValue/' +
      textSearch +
      '/' +
      alternativeVendor +
      '/' +
      paymentMethodType;
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส Country
  findCountryCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/country/getAll';
    } else {
      url = '/master/country/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส คีย์ธนาคาร
  findBankBranchCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/bank/branch/getAll';
    } else {
      url = '/master/bank/branch/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // รหัส PaymentMethod
  findPaymentMethodCodeWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/paymentMethod/getAll';
    } else {
      url = '/master/paymentMethod/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // สกุลเงิน
  findCurrencyWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/currency/getAll';
    } else {
      url = '/master/currency/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // houseBankPaymentMethod
  findHouseBankPaymentMethodWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/houseBank/paymentMethod/getAll';
    } else {
      url = '/master/houseBank/paymentMethod/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // houseBank
  findHouseBankWithParam(textSearch): Promise<any> {
    let url = '';
    if (textSearch === '') {
      url = '/master/houseBank/getAll';
    } else {
      url = '/master/houseBank/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // houseBankAccount
  findHouseBankAccountWithParam(textSearch): Promise<any> {
    console.log('============ /houseBank/account ==============');
    let url = '';
    if (textSearch === '') {
      url = '/master/houseBank/account/getAll';
    } else {
      url = '/master/houseBank/account/getByValue/' + textSearch;
    }

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  findOneHouseBankWithParam(compCode, houseBankKey, bankBranch): Promise<any> {
    const url = '/master/houseBank/getOne/' + compCode + '/' + houseBankKey + '/' + bankBranch;

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  findOneHouseBankAccountWithParam(compCode, houseBankKey, bankAccountNo): Promise<any> {
    const url =
      '/master/houseBank/account/getOne/' + compCode + '/' + houseBankKey + '/' + bankAccountNo;

    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  findOneCompanyCodeWithParam(compCode): Promise<any> {
    const url = '/master/compCode/getOne/' + compCode;
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  findAllNonBusinessDayByRangeAndDay(dateFrom, rangeDay): Promise<any> {
    const url = '/master/nonBusiness/findByDateAndRangeDay/' + dateFrom + '/' + rangeDay;
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  // คำนวน วันหยุด จาก วัน และ จำนวน
  findAllNonBusinessDay(): Promise<any> {
    const url = '/master/nonBusiness/getAll';
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  checkApUserChgBankAccNo(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/master/checkApUserChgBankAccNo', object)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  bankAccountDetail(vendorCode, value, routingNo): Promise<any> {
    const url = '/master/bankAccountDetail/getByValue/' + vendorCode + '/' + value + '/' + routingNo;
    return this.httpClient
      .get(`${environment.apiUrl}` + url)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }
}
