// import { Component, EventEmitter, Input, Output } from '@angular/core';
// import { Utils } from '@shared/utils/utils';
// import { FormGroup } from '@angular/forms';
//
// @Component({
//   template: '',
// })
// export abstract class ExportFileButtonComponentAbstract {
//   @Input() form?: FormGroup;
//   @Input() searchCriteria: any;
//   @Input() pathServiceExport: string;
//   @Input() fileNameExport: string;
//   @Input() isConvertDate: boolean;
//   @Input() setFileNameManual: boolean = true;
//   @Output() click = new EventEmitter<void>();
//   @Output() onValidate: EventEmitter<any> = new EventEmitter<any>();
//
//   constructor(private _utils: Utils) {}
//
//   protected parseCriteria() {
//     const columnYearConvert = ['BUDGET_YEAR', 'FISCAL_YEAR', 'YEAR'];
//     if (this.isConvertDate) {
//       this.searchCriteria = this._utils.convertYearCriteriaToAD(
//         this.searchCriteria,
//         columnYearConvert
//       );
//     }
//     return this._utils.parseStarToPercent({ ...this.searchCriteria });
//   }
// }
