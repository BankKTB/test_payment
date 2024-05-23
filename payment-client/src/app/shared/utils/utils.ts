import { CostCenterData } from '@core/models/cost-center-data';
import { PaymentCenterData } from '@core/models/payment-center-data';

export class Utils {
  public listPaymentCenters: PaymentCenterData[] = [];
  public listCostCenters: CostCenterData[] = [];
  monthTh = {
    1: 'ม.ค.',
    2: 'ก.พ.',
    3: 'มี.ค.',
    4: 'เม.ย.',
    5: 'พ.ค.',
    6: 'มิ.ย.',
    7: 'ก.ค.',
    8: 'ส.ค.',
    9: 'ก.ย.',
    10: 'ต.ค.',
    11: 'พ.ย.',
    12: 'ธ.ค.',
  };
  monthFullTh = {
    1: 'มกราคม',
    2: 'กุมภาพันธ์',
    3: 'มีนาคม',
    4: 'เมษายน',
    5: 'พฤษภาคม',
    6: 'มิถุนายน',
    7: 'กรกฎาคม',
    8: 'สิงหาคม',
    9: 'กันยายน',
    10: 'ตุลาคม',
    11: 'พฤศจิกายน',
    12: 'ธันวาคม',
  };
  dayFull = {
    Mon: 'จ.',
    Tue: 'อ.',
    Wed: 'พ.',
    Thu: 'พฤ.',
    Fri: 'ศ.',
    Sat: 'ส.',
    Sun: 'อา.',
  };

  public current = new Date().getFullYear() + 543;
  public listYear = [];
  public fiscYear = 0;
  public fiscPeriod = [];
  private past = this.current - 6;
  private future = this.current + 8;

  public isEmpty(value) {
    if (value !== null && value !== '') {
      return false;
    } else {
      return true;
    }
  }

  public CalculateYear() {
    const listYear = [];
    for (let i = this.past; i <= this.future; i++) {
      listYear.push({ id: i, name: i });
    }
    return listYear;
  }

  public CalculateFiscYear(date: Date) {
    const month = date.getMonth() + 1;
    let year = date.getFullYear();
    if (month >= 10) {
      return (year = date.getFullYear() + 544); // old code
    } else {
      return year + 543; // old code
    }
  }

  public parseDate(day, month, year) {
    day = +day < 10 ? '0' + day : day;
    month = +month < 10 ? '0' + month : month;
    return year + '-' + month + '-' + day;
  }

  public parseDateForParameter(day, month, year) {
    console.log('parseDateForParameter');
    console.log(day);
    console.log(month);
    console.log(year);
    day = +day < 10 ? '0' + day : day;
    month = +month < 10 ? '0' + month : month;
    // return year.toString() + month.toString() + day.toString();
    return day.toString() + month.toString() + (Number(year) + 543).toString();
  }

  public convertYearToAD(year: string): string {
    console.log('y', year);
    if (year) {
      const buddhistYear = Number(year);
      const adYear = buddhistYear - 543;
      return adYear.toString();
    } else {
      return new Date().getFullYear.toString();
    }
  }

  public convertYearToBuddhist(year: string): number {
    if (year) {
      const AD = Number(year);
      const buddhistYear = AD + 543;
      return buddhistYear;
    } else {
      return new Date().getFullYear();
    }
  }

  public convertCompanyTextToCompanyArray(companyText: string): any {
    console.log(companyText);
    const arraySplitFirst = companyText.split(',');
    const listCompany = [];
    let flag = false;
    const data = {
      companyFrom: '',
      companyTo: '',
    };
    arraySplitFirst.forEach((item) => {
      console.log('======================');
      if (flag) {
        if (item.endsWith(')')) {
          flag = false;
          data.companyTo = item ? item.replace(')', '') : '';
        }
      } else {
        if (item.startsWith('(')) {
          flag = true;
          data.companyFrom = item ? item.replace('(', '') : '';
        } else if (item.endsWith(')')) {
        } else {
          flag = false;
          data.companyFrom = item;
          data.companyTo = '';
        }
      }
      // console.log(test);
      if (!flag) {
        const newValue = {
          companyFrom: data.companyFrom,
          companyTo: data.companyTo,
        };
        if (newValue.companyFrom) {
          listCompany.push(newValue);
        }
      }
    });
    console.log(listCompany);

    return listCompany;
  }

  public chanTest() {
    const text = '(33333,44444),30000,(33333,44444),12005,32005,(33333,44444)';

    const a = text.split(',');

    console.log(a);
    const listCompany = [];
    a.forEach((x, index) => {
      if (x.indexOf('(') !== -1) {
        const test = {
          companyFrom: x.replace('(', ''),
          companyTo: a[index + 1].replace(')', ''),
        };
        listCompany.push(test);
      } else if (x.indexOf(')') !== -1) {
      } else {
        const test = {
          companyFrom: x.replace('(', ''),
          companyTo: '',
        };
        listCompany.push(test);
      }
    });

    console.log(listCompany);
  }

  public convertCompanyArrayToCompanyText(companyArray: any): any {
    let companyText = '';
    companyArray.forEach((item) => {
      let textString = '';
      if (item.companyFrom && item.companyTo) {
        textString += '(' + item.companyFrom + ',' + item.companyTo + ')';
      } else {
        if (item.companyFrom) {
          textString += item.companyFrom;
        }
        if (item.companyTo) {
          textString += ',' + item.companyTo;
        }
      }
      companyText += textString + ',';
    });
    if (companyText.endsWith(',')) {
      companyText = companyText.substring(0, companyText.length - 1);
    }
    return companyText;
  }

  public convertConditionFieldTextToConditionFieldArray(conditionFieldText: string): any {
    console.log(conditionFieldText);
    const listConditionField = [];
    const data = {
      conditionFieldFrom: '',
      conditionFieldTo: '',
    };
    if (conditionFieldText) {
      const arraySplitFirst = conditionFieldText.split(',');

      let flag = false;

      arraySplitFirst.forEach((item) => {
        console.log('======================');
        if (flag) {
          if (item.endsWith(')')) {
            flag = false;
            data.conditionFieldTo = item ? item.replace(')', '') : '';
          }
        } else {
          if (item.startsWith('(')) {
            flag = true;
            data.conditionFieldFrom = item ? item.replace('(', '') : '';
          } else if (item.endsWith(')')) {
          } else {
            flag = false;
            data.conditionFieldFrom = item;
            data.conditionFieldTo = '';
          }
        }
        // console.log(test);
        if (!flag) {
          const newValue = {
            conditionFieldFrom: data.conditionFieldFrom,
            conditionFieldTo: data.conditionFieldTo,
          };
          listConditionField.push(newValue);
        }
      });
      return listConditionField;
    } else {
      listConditionField.push(data);
      return listConditionField;
    }
  }

  public convertConditionFieldTextToConditionFieldArrayHaveExclude(
    conditionFieldText: string
  ): any {
    const arraySplitFirst = conditionFieldText.split(',');
    const listConditionField = [];
    let flag = false;
    const data = {
      conditionFieldFrom: '',
      conditionFieldTo: '',
    };
    arraySplitFirst.forEach((item) => {
      console.log('======================');
      if (flag) {
        if (item.endsWith(')')) {
          flag = false;
          data.conditionFieldTo = item ? item.replace(')', '') : '';
        }
      } else {
        if (item.startsWith('(')) {
          flag = true;
          data.conditionFieldFrom = item ? item.replace('(', '') : '';
        } else if (item.endsWith(')')) {
        } else {
          flag = false;
          data.conditionFieldFrom = item;
          data.conditionFieldTo = '';
        }
      }
      // console.log(test);
      if (!flag) {
        const newValue = {
          conditionFieldFrom: data.conditionFieldFrom,
          conditionFieldTo: data.conditionFieldTo,
        };
        listConditionField.push(newValue);
      }
    });
    return listConditionField;
  }

  public convertConditionFieldArrayToConditionFieldText(conditionFieldArray: any): any {
    let conditionFieldText = '';
    conditionFieldArray.forEach((item) => {
      let textString = '';
      if (item.conditionFieldFrom && item.conditionFieldTo) {
        textString += '(' + item.conditionFieldFrom + ',' + item.conditionFieldTo + ')';
      } else {
        if (item.conditionFieldFrom) {
          textString += item.conditionFieldFrom;
        }
        if (item.conditionFieldTo) {
          textString += ',' + item.conditionFieldTo;
        }
      }
      conditionFieldText += textString + ',';
    });
    if (conditionFieldText.endsWith(',')) {
      conditionFieldText = conditionFieldText.substring(0, conditionFieldText.length - 1);
    }
    return conditionFieldText;
  }

  format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'input') {
      let day: string = date.getDate().toString();
      day = +day < 10 ? '0' + day : day;
      let month: string = this.monthFullTh[date.getMonth() + 1].toString();
      month = +month < 10 ? '0' + month : month;
      let year;
      year = date.getFullYear() + 543;
      return `${day} ${month} ${year}`;
    }
    const dayFullName = this.dayFull[date.toDateString().split(' ')[0]];
    const day: string = date.getDate() ? date.getDate().toString() : '01';
    const month: string = this.monthTh[date.getMonth() + 1]
      ? this.monthTh[date.getMonth() + 1].toString()
      : this.monthTh[1];
    const year = date.getFullYear() + 543;
    return `${dayFullName} ${day} ${month} ${year}`;
  }

  public displayYearSourceMoneyBySourceMoneyCode(sourceMoneyCode) {
    const year = '25';
    let yearSourceMoney = '';
    if (sourceMoneyCode) {
      yearSourceMoney = year + sourceMoneyCode.substr(0, 2);
    }

    return yearSourceMoney;
  }

  getNonBusinessDay() {}

  b64toBlob(dataURI) {
    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
  }

  public convertBase64ToByteExcel(base64String) {
    const mediaType = `data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,${base64String}`;
    return this.b64toBlob(mediaType);
  }

  public convertBase64ToBytePdf(base64String) {
    const byteCharacters = atob(base64String);
    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
      const slice = byteCharacters.slice(offset, offset + 512);

      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, {
      type: 'application/pdf',
    });
  }

  public convertYearCriteriaToAD(searchCriteria: any, columnYearConvert: any): any {
    Object.keys(searchCriteria).forEach((key, parentIndex) => {
      if (columnYearConvert.indexOf(key) > -1) {
        const listConvert = searchCriteria[key];
        if (listConvert instanceof Array) {
          listConvert.map((item, index) => {
            if (item.from) {
              searchCriteria[key][index].from = this.convertYearToAD(item.from);
            }
            if (item.to) {
              searchCriteria[key][index].to = this.convertYearToAD(item.to);
            }
          });
        } else if (listConvert instanceof Object) {
          if (listConvert.from) {
            if (listConvert.from) {
              searchCriteria[parentIndex].from = this.convertYearToAD(listConvert.from);
            }
            if (listConvert.to) {
              searchCriteria[parentIndex].to = this.convertYearToAD(listConvert.to);
            }
          } else {
            listConvert.value.map((item, index) => {
              if (item.from) {
                searchCriteria[parentIndex].value[index].from = this.convertYearToAD(
                  listConvert.from
                );
              }
              if (item.to) {
                searchCriteria[parentIndex].value[index].to = this.convertYearToAD(listConvert.to);
              }
            });
          }
        } else {
          searchCriteria[key] = this.convertYearToAD(listConvert);
        }
      }
    });
    return searchCriteria;
  }

  public parseStarToPercent(searchCriteria: any): any {
    const keyDefaultArray = ['groupCriteria', 'pageCriteria', 'sortCriterias', 'summaryCriteria'];
    Object.keys(searchCriteria).forEach((key, parentIndex) => {
      if (keyDefaultArray.indexOf(key) < 0) {
        const listConvert = searchCriteria[key];
        if (!this.isEmpty(listConvert)) {
          if (listConvert instanceof Array) {
            listConvert.map((item, index) => {
              if (typeof item.from === 'string') {
                if (item.from) {
                  searchCriteria[key][index].from = item.from
                    ? item.from.toString().replaceAll('*', '%').replaceAll('+', '_')
                    : item.from;
                }
                if (item.to) {
                  searchCriteria[key][index].to = item.to
                    ? item.to.toString().replaceAll('*', '%').replaceAll('+', '_')
                    : item.to;
                }
              } else {
              }
            });
          } else if (listConvert instanceof Object) {
            if (listConvert.from) {
              if (listConvert.from) {
                searchCriteria[key].from = listConvert.from
                  ? listConvert.from.toString().replaceAll('*', '%').replaceAll('+', '_')
                  : listConvert.from;
              }
              if (listConvert.to) {
                searchCriteria[key].to = listConvert.to
                  ? listConvert.to.toString().replaceAll('*', '%').replaceAll('+', '_')
                  : listConvert.to;
              }
            } else if (listConvert.value) {
              listConvert.value.map((item, index) => {
                if (typeof item.from === 'string') {
                  if (item.from) {
                    searchCriteria[parentIndex].value[index].from = item.from
                      ? item.from.toString().replaceAll('*', '%').replaceAll('+', '_')
                      : item.from;
                  }
                  if (item.to) {
                    searchCriteria[parentIndex].value[index].to = item.to
                      ? item.to.toString().replaceAll('*', '%').replaceAll('+', '_')
                      : item.to;
                  }
                }
              });
            }
          } else {
            if (!('boolean' === typeof listConvert)) {
              searchCriteria[key] = listConvert
                ? listConvert.toString().replaceAll('*', '%').replaceAll('+', '_')
                : listConvert;
            }
          }
        }
      }
    });
    return searchCriteria;
  }

  public async copyToClipboard(event, listCriteriaRange) {
    if (window['clipboardData']) {
      // FOR IE
      let value = window['clipboardData'].getData('Text');
    } else {
      // for other navigators
      await navigator['clipboard']
        .readText()
        .then((clipText) => {
          if (clipText) {
            const listClipBoard = clipText.split('\n');
            listClipBoard.forEach((value: string) => {
              if (value && value.length > 0) {
                const textLines = value.replace('\r', '');
                if (textLines.length > 0) {
                  const textLine = textLines.split('\t');
                  if (textLine.length > 0) {
                    listCriteriaRange.push({
                      from: textLine[0] ? textLine[0] : null,
                      to: textLine[1] ? textLine[1] : null,
                      optionExclude: false,
                    });
                  }
                }
              }
            });
          }
        })
        .then(() => {
          return new Promise((resolve) => {
            listCriteriaRange = listCriteriaRange.filter((data) => data.from !== null);
            return resolve(listCriteriaRange);
          });
        });
    }
  }

  public onEnterFilter(value, dataSource) {
    let filterValue = value || '';
    if (this.validateDate(filterValue)) {
      let separateSyntax = '';
      if (filterValue.indexOf('.') > 0) separateSyntax = '.';
      if (filterValue.indexOf('/') > 0) separateSyntax = '/';
      if (filterValue.indexOf('-') > 0) separateSyntax = '-';

      const dateList = filterValue.split(separateSyntax);
      if (dateList.length < 3) return;
      const convertYear = Number(dateList[2]) - 543;
      filterValue = new Date(
        `${dateList[0]}${separateSyntax}${dateList[1]}${separateSyntax}${convertYear}`
      )
        .getTime()
        .toString();
    } else {
      filterValue = filterValue.trim(); // Remove whitespace
      filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    }
    return (dataSource.filter = filterValue);
  }

  private validateDate(dateStr: string): boolean {
    const date_regex = /^([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1][0-2])[./-]([0-9]{4}|[0-9]{2})$/;
    return date_regex.test(dateStr);
  }
  public bcCalculateFiscPeriod(date: Date) {
    this.fiscPeriod = [];
    const listFiscPeriod = [];
    const relationshipPeriod = new Map();
    relationshipPeriod.set('1', '4');
    relationshipPeriod.set('2', '5');
    relationshipPeriod.set('3', '6');
    relationshipPeriod.set('4', '7');
    relationshipPeriod.set('5', '8');
    relationshipPeriod.set('6', '9');
    relationshipPeriod.set('7', '10');
    relationshipPeriod.set('8', '11');
    relationshipPeriod.set('9', '12');
    relationshipPeriod.set('10', '1');
    relationshipPeriod.set('11', '2');
    relationshipPeriod.set('12', '3');

    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    if (month === 9 && day === 30) {
      listFiscPeriod.push('12');
      listFiscPeriod.push('13');
      listFiscPeriod.push('14');
      listFiscPeriod.push('15');
      listFiscPeriod.push('16');
      this.fiscPeriod = listFiscPeriod;
      return listFiscPeriod;
    } else {
      const result = relationshipPeriod.get(month.toString());
      listFiscPeriod.push(result);
      this.fiscPeriod = listFiscPeriod;
      return listFiscPeriod;
    }
  }
}
