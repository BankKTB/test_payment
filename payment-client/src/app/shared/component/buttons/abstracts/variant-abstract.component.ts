// import { Component, EventEmitter, Input, Output } from '@angular/core';
// import { FormGroup } from '@angular/forms';
// import { Constant } from '@shared/utils/constant';
//
// @Component({
//   template: '',
// })
// export abstract class VariantAbstractComponent {
//   @Input() formGroup: FormGroup;
//   @Input() reportCode: string;
//   @Input() searchCriteria: any;
//   @Input() criteriaDesc: any;
//   @Input() variantId: any;
//   @Output() onSubmit: EventEmitter<any> = new EventEmitter<any>();
//   @Output() onValidate: EventEmitter<any> = new EventEmitter<any>();
//   public _constant: Constant;
//
//   protected constructor() {
//     this._constant = new Constant();
//   }
//
//   protected getDialogMasterConfig() {
//     return {
//       width: this._constant.WIDTH_DIALOG_VARIANT,
//       panelClass: this._constant.PANEL_CLASS_DIALOG_MASTER,
//     };
//   }
// }
