import { ThaiOnlyDirective } from '@shared/directives/thai-only.directive';
import { NumberOnlyDirective } from '@shared/directives/number-only.directive';
import { CommonModule } from '@angular/common';
import { ThaiyearPipe } from '@shared/pipe/thaiyear.pipe';
import { HideDirective } from '@shared/directives/hide.directive';
import { LoadingComponent } from '@shared/component/loading/loading.component';
import { CustomDecimalPipe } from '@shared/pipe/custom-decimal.pipe';
import { EnglishOnlyDirective } from '@shared/directives/english-only.directive';
import { UpperCaseDirectiveDirective } from '@shared/directives/upper-case-directive.directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CurrencyDirective } from '@shared/directives/currency.directive';
import { ThaidatePipe } from '@shared/pipe/thaidate.pipe';
import { NgModule } from '@angular/core';
import { HeadContentComponent } from '@shared/component/head-content/head-content.component';
import { PostingKeyPipe } from '@shared/pipe/posting-key.pipe';
import { FooterComponent } from '@shared/component/footer/footer.component';
import { XmlPipe } from '@shared/pipe/xml.pipe';
import { HttpClientModule } from '@angular/common/http';
import { JsonPipe } from '@shared/pipe/json.pipe';
import { CurrencyPipe } from '@shared/pipe/currency.pipe';
import { DragDropModule } from '@angular/cdk/drag-drop';
import {
  MatAutocompleteModule,
  MatCardModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
} from '@angular/material';
import { HeaderComponent } from '@shared/component/header/header.component';
import { SidebarComponent } from '@shared/component/sidebar/sidebar.component';
import { DatepickerHeaderComponent } from '@shared/component/datepicker-header/datepicker-header.component';
import { DialogSearchVendorComponent } from '@shared/component/dialog-param/dialog-search-vendor/dialog-search-vendor.component';
import { DialogDetailDocumentComponent } from '@shared/component/dialog-detail-document/dialog-detail-document.component';
import { DialogSearchMasterComponent } from '@shared/component/dialog-search-master/dialog-search-master.component';
import { DialogSaveParameterComponent } from '@shared/component/dialog-status/dialog-save-parameter/dialog-save-parameter.component';
import { DialogSearchFieldComponent } from '@shared/component/dialog-additional/dialog-search-field/dialog-search-field.component';
import { DialogSearchPaymentMethodComponent } from '@shared/component/dialog-param/dialog-search-payment-method/dialog-search-payment-method.component';
import { DialogCompanyPayeeConfigComponent } from '@shared/component/dialog-company-payee-config/dialog-company-payee-config.component';
import { DialogSearchVendorPaymentComponent } from '@shared/component/dialog-search-vendor-payment/dialog-search-vendor-payment.component';
import { DialogSearchParameterComponent } from '@shared/component/dialog-search-parameter/dialog-search-parameter.component';
import { DialogCopyParameterComponent } from '@shared/component/dialog-copy-parameter/dialog-copy-parameter.component';
import { DialogCompanyPayingConfigComponent } from '@shared/component/dialog-company-paying-config/dialog-company-paying-config.component';
import { DialogPayMethodConfigComponent } from '@shared/component/dialog-pay-method-config/dialog-pay-method-config.component';
import { DialogPayMethodCurrencyConfigComponent } from '@shared/component/dialog-pay-method-currency-config/dialog-pay-method-currency-config.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogRunProcessComponent } from '@shared/component/dialog-run-process/dialog-run-process.component';
import { DialogWarningAddtiionalLogComponent } from '@shared/component/dialog-additional/dialog-warning-addtiional-log/dialog-warning-addtiional-log.component';
import { DialogCopyCompanyPayeeComponent } from '@shared/component/dialog-copy-company-payee/dialog-copy-company-payee.component';
import { DialogCopyPaymentMethodConfigComponent } from '@shared/component/dialog-copy-payment-method-config/dialog-copy-payment-method-config.component';
import { DialogCopyPaymentMethodCountryConfigComponent } from '@shared/component/dialog-copy-payment-method-country-config/dialog-copy-payment-method-country-config.component';
import { DialogOmColumnTableComponent } from '@shared/component/dialog-om-column-table/dialog-om-column-table.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { IntlModule } from '@progress/kendo-angular-intl';
import { DateInputsModule } from '@progress/kendo-angular-dateinputs';
import { TabAdditionalLogComponent } from '@shared/component/tab-additional-log/tab-additional-log.component';
import { TabIndependentComponent } from '@shared/component/tab-independent/tab-independent.component';
import { TabParameterComponent } from '@shared/component/tab-parameter/tab-parameter.component';
import { TabStatusComponent } from '@shared/component/tab-status/tab-status.component';
import { DialogShowProposalDocumentComponent } from './component/dialog-show-proposal-document/dialog-show-proposal-document.component';
import { DialogOmSearchCriteriaComponent } from './component/dialog-om-search-criteria/dialog-om-search-criteria.component';
import { DialogOmSaveSearchCriteriaComponent } from './component/dialog-om-save-search-criteria/dialog-om-save-search-criteria.component';
import { DialogOmSearchColumnTableComponent } from './component/dialog-om-search-column-table/dialog-om-search-column-table.component';
import { DialogOmSaveColumnTableComponent } from './component/dialog-om-save-column-table/dialog-om-save-column-table.component';
import { DialogOmChangePaymentBlockComponent } from './component/dialog-om-change-payment-block/dialog-om-change-payment-block.component';
import { DialogDetailDocumentColumnTableComponent } from './component/dialog-detail-document-column-table/dialog-detail-document-column-table.component';
import { DialogSearchParameterGenerateFileComponent } from './component/dialog-search-parameter-generate-file/dialog-search-parameter-generate-file.component';
import { DialogOmCompanyCodeComponent } from '@shared/component/dialog-om-company-code/dialog-om-company-code.component';
import { DialogEditSmartFeeComponent } from './component/dialog-edit-smart-fee/dialog-edit-smart-fee.component';
import { DialogEditSwiftFeeComponent } from './component/dialog-edit-swift-fee/dialog-edit-swift-fee.component';
import { DialogEditSumFileConditionComponent } from './component/dialog-edit-sum-file-condition/dialog-edit-sum-file-condition.component';
import { DialogSaveSumFileConditionComponent } from './component/dialog-save-sum-file-condition/dialog-save-sum-file-condition.component';
import { DialogSaveSmartFeeComponent } from './component/dialog-save-smart-fee/dialog-save-smart-fee.component';
import { DialogSaveSwiftFeeComponent } from './component/dialog-save-swift-fee/dialog-save-swift-fee.component';
import { DialogOmDocTypeComponent } from './component/dialog-om-doc-type/dialog-om-doc-type.component';
import { DialogOmPaymentMethodComponent } from './component/dialog-om-payment-method/dialog-om-payment-method.component';
import { DialogConfirmDeleteMasterComponent } from './component/dialog-confirm-delete-master/dialog-confirm-delete-master.component';
import { DialogEditJobSchedComponent } from '@shared/component/dialog-edit-job-sched/dialog-edit-job-sched.component';
import { DialogOmLogComponent } from './component/dialog-om-log/dialog-om-log.component';
import { DialogConfirmStatusUsabilityComponent } from './component/dialog-confirm-status-usability/dialog-confirm-status-usability.component';
import { DialogPaymentRunErrorDetailComponent } from './component/dialog-payment-run-error-detail/dialog-payment-run-error-detail.component';
import { DialogReversePaymentComponent } from './component/dialog-reverse-payment/dialog-reverse-payment.component';
import { DialogShowAutodocComponent } from './component/dialog-show-autodoc/dialog-show-autodoc.component';
import { DialogReturnParameterGenFileComponent } from './component/dialog-return-parameter-gen-file/dialog-return-parameter-gen-file.component';
import { TableReturnUpdateStatusComponent } from './component/table-return-update-status/table-return-update-status.component';
import { DialogResultComponent } from './component/dialog-result/dialog-result.component';
import { TableReturnReversePaymentComponent } from './component/table-return-reverse-payment/table-return-reverse-payment.component';
import { TableReturnReverseInvoiceComponent } from './component/table-return-reverse-invoice/table-return-reverse-invoice.component';
import { DialogArrangeColumnComponent } from './component/dialog-arrange-column/dialog-arrange-column.component';
import { DialogSortColumnComponent } from './component/dialog-sort-column/dialog-sort-column.component';
import { DialogFilterColumnComponent } from './component/dialog-filter-column/dialog-filter-column.component';
import { DialogCriteriaComponent } from './component/dialog-criteria/dialog-criteria.component';
import { DialogReturnLogComponent } from './component/dialog-return-log/dialog-return-log.component';
import { DatePatternPipe } from './pipe/date-pattern.pipe';
import { NumberSequencePipe } from './pipe/number-sequence.pipe';
import { DialogTaxFeeComponent } from './component/dialog-tax-fee/dialog-tax-fee.component';
import { DialogOmSearchDetailComponent } from './component/dialog-om-search-detail/dialog-om-search-detail.component';
import { DialogConfirmComponent } from './component/dialog-confirm/dialog-confirm.component';
import { DialogOmVendorComponent } from '@shared/component/dialog-om-vendor/dialog-om-vendor.component';
import { DialogOmPaymentCenterComponent } from '@shared/component/dialog-om-payment-center/dialog-om-payment-center.component';
import { DialogOmSpecialTypeComponent } from '@shared/component/dialog-om-special-type/dialog-om-special-type.component';
import { InputDateDirective } from './directives/input/input-date.directive';
import { DialogReversePaymentAllComponent } from './component/dialog-reverse-payment-all/dialog-reverse-payment-all.component';
import { DialogMultipleSelectDocumentComponent } from './component/dialog-multiple-select-document/dialog-multiple-select-document.component';
import { DialogPreviewSelectGroupDocumentComponent } from './component/dialog-preview-select-group-document/dialog-preview-select-group-document.component';
import { DialogUploadComponent } from './component/dialog-upload/dialog-upload.component';
import { DialogSearchParameterReturnFileComponent } from '@shared/component/dialog-search-parameter-return-file/dialog-search-parameter-return-file.component';
import { ReasonReversePipe } from './pipe/reason-reverse.pipe';
import { DialogConfirmChangeDocumentComponent } from './component/dialog-confirm-change-document/dialog-confirm-change-document.component';
import { TimePatternPipe } from './pipe/time-pattern.pipe';
import { DialogAuthenticationConfirmComponent } from './component/dialog-authentication-confirm/dialog-authentication-confirm.component';
import { DetailPaymentBlockPipe } from './pipe/detail-payment-block.pipe';
import { DialogBatchTransactionPaymentNameComponent } from './component/dialog-batch-transaction-payment-name/dialog-batch-transaction-payment-name.component';
import { DialogOmFiAreaComponent } from './component/dialog-om-fi-area/dialog-om-fi-area.component';
import { DialogSearchJobParameterComponent } from './component/dialog-search-job-parameter/dialog-search-job-parameter.component';
import { DialogPoItemDetailComponent } from './component/dialog-po-item-detail/dialog-po-item-detail.component';
import { DialogPoHeaderDetailComponent } from './component/dialog-po-header-detail/dialog-po-header-detail.component';
import { DialogPoTaxComponent } from './component/dialog-po-tax/dialog-po-tax.component';
import { DialogPoAdvancePaymentComponent } from './component/dialog-po-advance-payment/dialog-po-advance-payment.component';
import { DialogBankAccountDetailComponent } from './component/dialog-bank-account-detail/dialog-bank-account-detail.component';
import { DialogPoChangeHistoryComponent } from './component/dialog-po-change-history/dialog-po-change-history.component';
import { DialogPoHistoryComponent } from './component/dialog-po-history/dialog-po-history.component';
import { DialogWarningComponent } from './component/dialog-warning/dialog-warning.component';
import { ExportExcelButtonComponent } from './component/buttons/export-excel-button/export-excel-button.component';
import { ExportPdfButtonComponent } from './component/buttons/export-pdf-button/export-pdf-button.component';
import { DialogUploadChangeDocumentComponent } from '@shared/component/dialog-upload-change-document/dialog-upload-change-document.component';
import { BaseTextAreaComponent } from './component/inputs/base-text-area/base-text-area.component';
import { DialogGenerateFileResultComponent } from './component/dialog-generate-file-result/dialog-generate-file-result.component';
import { DialogVendorForPaymentComponent } from './component/dialog-vendor-for-payment/dialog-vendor-for-payment.component';
import { DialogOmDocumentNoComponent } from '@shared/component/dialog-om-document-no/dialog-om-document-no.component';
import { DialogOmHeaderReferenceComponent } from './component/dialog-om-header-reference/dialog-om-header-reference.component';
import { DialogOmInputReasonUnapprovedComponent } from './component/dialog-om-input-reason-unapproved/dialog-om-input-reason-unapproved.component';
import { BackToTopComponent } from './component/back-to-top/back-to-top.component';
import { DialogShowDocumentReferenceComponent } from './component/dialog-show-document-reference/dialog-show-document-reference.component';
import { DialogShowErrorOmComponent } from './component/dialog-show-error-om/dialog-show-error-om.component';
import { DialogDisplayArrangeColumnComponent } from './component/dialog-display-arrange-column/dialog-display-arrange-column.component';
import { DialogSaveArrangeColumnComponent } from './component/dialog-save-arrange-column/dialog-save-arrange-column.component';
import { DialogUpdateArrangeColumnComponent } from './component/dialog-update-arrange-column/dialog-update-arrange-column.component';
import { DeleteButtonComponent } from './component/buttons/delete-button/delete-button.component';

@NgModule({
  declarations: [
    HeadContentComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    LoadingComponent,

    NumberOnlyDirective,
    CurrencyDirective,
    UpperCaseDirectiveDirective,
    EnglishOnlyDirective,
    ThaiOnlyDirective,

    CurrencyPipe,
    ThaidatePipe,
    ThaiyearPipe,
    PostingKeyPipe,
    XmlPipe,
    JsonPipe,
    CustomDecimalPipe,
    HideDirective,

    DatepickerHeaderComponent,
    DialogSearchVendorComponent,
    DialogSaveParameterComponent,
    DialogSearchMasterComponent,
    DialogDetailDocumentComponent,
    DialogSearchFieldComponent,
    DialogSearchPaymentMethodComponent,
    DialogCopyParameterComponent,
    DialogSearchParameterComponent,
    DialogSearchVendorPaymentComponent,
    DialogCompanyPayeeConfigComponent,
    DialogCompanyPayingConfigComponent,
    DialogPayMethodConfigComponent,
    DialogPayMethodCurrencyConfigComponent,
    DialogSearchMasterDataComponent,
    DialogRunProcessComponent,
    DialogWarningAddtiionalLogComponent,
    DialogCopyCompanyPayeeComponent,
    DialogCopyPaymentMethodConfigComponent,
    DialogCopyPaymentMethodCountryConfigComponent,
    DialogOmColumnTableComponent,
    TabAdditionalLogComponent,
    TabIndependentComponent,
    TabParameterComponent,
    TabStatusComponent,
    DialogShowProposalDocumentComponent,
    DialogOmSearchCriteriaComponent,
    DialogOmSaveSearchCriteriaComponent,
    DialogOmSearchColumnTableComponent,
    DialogOmSaveColumnTableComponent,
    DialogOmChangePaymentBlockComponent,
    DialogDetailDocumentColumnTableComponent,
    DialogSearchParameterGenerateFileComponent,
    DialogSearchParameterReturnFileComponent,
    DialogOmCompanyCodeComponent,
    DialogEditSmartFeeComponent,
    DialogEditSwiftFeeComponent,
    DialogEditSumFileConditionComponent,
    DialogSaveSumFileConditionComponent,
    DialogSaveSmartFeeComponent,
    DialogSaveSwiftFeeComponent,
    DialogOmDocTypeComponent,
    DialogOmPaymentMethodComponent,
    DialogConfirmDeleteMasterComponent,
    DialogEditJobSchedComponent,
    DialogOmLogComponent,
    DialogConfirmStatusUsabilityComponent,
    DialogPaymentRunErrorDetailComponent,
    DialogReversePaymentComponent,
    DialogShowAutodocComponent,
    DialogReturnParameterGenFileComponent,
    TableReturnUpdateStatusComponent,
    DialogResultComponent,
    TableReturnReversePaymentComponent,
    TableReturnReverseInvoiceComponent,
    DialogArrangeColumnComponent,
    DialogSortColumnComponent,
    DialogFilterColumnComponent,
    DialogCriteriaComponent,
    DialogReturnLogComponent,
    DatePatternPipe,
    NumberSequencePipe,
    DialogTaxFeeComponent,
    DialogOmSearchDetailComponent,
    DialogConfirmComponent,
    DialogOmVendorComponent,
    DialogOmPaymentCenterComponent,
    DialogOmSpecialTypeComponent,
    InputDateDirective,
    DialogReversePaymentAllComponent,
    DialogMultipleSelectDocumentComponent,
    DialogPreviewSelectGroupDocumentComponent,
    DialogUploadComponent,
    ReasonReversePipe,
    DialogConfirmChangeDocumentComponent,
    TimePatternPipe,
    DialogAuthenticationConfirmComponent,
    DetailPaymentBlockPipe,
    DialogBatchTransactionPaymentNameComponent,
    DialogOmFiAreaComponent,
    DialogSearchJobParameterComponent,
    DialogPoItemDetailComponent,
    DialogPoHeaderDetailComponent,
    DialogPoTaxComponent,
    DialogPoAdvancePaymentComponent,
    DialogBankAccountDetailComponent,
    DialogPoChangeHistoryComponent,
    DialogPoHistoryComponent,
    DialogWarningComponent,
    ExportExcelButtonComponent,
    ExportPdfButtonComponent,
    DialogUploadChangeDocumentComponent,
    BaseTextAreaComponent,
    DialogGenerateFileResultComponent,
    DialogVendorForPaymentComponent,
    DialogOmDocumentNoComponent,
    DialogOmHeaderReferenceComponent,
    DialogOmInputReasonUnapprovedComponent,
    BackToTopComponent,
    DialogShowDocumentReferenceComponent,
    DialogShowErrorOmComponent,
    DialogDisplayArrangeColumnComponent,
    DialogSaveArrangeColumnComponent,
    DialogUpdateArrangeColumnComponent,
    DeleteButtonComponent,
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    DragDropModule,
    ReactiveFormsModule,
    FormsModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatAutocompleteModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDialogModule,
    DragDropModule,
    FormsModule,
    ScrollingModule,
    MatRadioModule,
    MatSortModule,
    MatSidenavModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatListModule,
    IntlModule,
    DateInputsModule,
    MatPaginatorModule,
  ],
  exports: [
    HttpClientModule,
    HeadContentComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    LoadingComponent,
    NumberOnlyDirective,
    CurrencyDirective,
    EnglishOnlyDirective,
    ThaiOnlyDirective,
    UpperCaseDirectiveDirective,
    HideDirective,
    JsonPipe,
    XmlPipe,
    PostingKeyPipe,
    CustomDecimalPipe,
    ThaidatePipe,
    DatePatternPipe,
    NumberSequencePipe,
    ThaiyearPipe,
    ReasonReversePipe,
    DetailPaymentBlockPipe,

    CommonModule,
    HttpClientModule,
    DragDropModule,
    ReactiveFormsModule,
    FormsModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatAutocompleteModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDialogModule,
    DragDropModule,
    FormsModule,
    ScrollingModule,
    MatRadioModule,
    MatSortModule,
    MatSidenavModule,
    MatProgressSpinnerModule,
    IntlModule,
    DateInputsModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatListModule,
    TabAdditionalLogComponent,
    TabIndependentComponent,
    TabParameterComponent,
    TabStatusComponent,
    TableReturnUpdateStatusComponent,
    TableReturnReversePaymentComponent,
    TableReturnReverseInvoiceComponent,
    InputDateDirective,
    TimePatternPipe,
    ExportExcelButtonComponent,
    ExportPdfButtonComponent,
    BaseTextAreaComponent,
    BackToTopComponent,
  ],
  providers: [],
  entryComponents: [
    DatepickerHeaderComponent,
    DatepickerHeaderComponent,
    DialogSearchVendorComponent,
    DialogSaveParameterComponent,
    DialogSearchMasterComponent,
    DialogDetailDocumentComponent,
    DialogSearchFieldComponent,
    DialogSearchPaymentMethodComponent,
    DialogCopyParameterComponent,
    DialogSearchParameterComponent,
    DialogSearchVendorPaymentComponent,
    DialogCompanyPayeeConfigComponent,
    DialogCompanyPayingConfigComponent,
    DialogPayMethodConfigComponent,
    DialogPayMethodCurrencyConfigComponent,
    DialogSearchMasterDataComponent,
    DialogRunProcessComponent,
    DialogWarningAddtiionalLogComponent,
    DialogCopyCompanyPayeeComponent,
    DialogCopyPaymentMethodConfigComponent,
    DialogCopyPaymentMethodCountryConfigComponent,
    DialogOmColumnTableComponent,
    TabAdditionalLogComponent,
    TabIndependentComponent,
    TabParameterComponent,
    TabStatusComponent,
    DialogShowProposalDocumentComponent,
    DialogOmSearchCriteriaComponent,
    DialogOmSaveSearchCriteriaComponent,
    DialogOmSearchColumnTableComponent,
    DialogOmSaveColumnTableComponent,
    DialogOmChangePaymentBlockComponent,
    DialogDetailDocumentColumnTableComponent,
    DialogSearchParameterGenerateFileComponent,
    DialogSearchParameterReturnFileComponent,
    DialogOmCompanyCodeComponent,
    DialogEditSmartFeeComponent,
    DialogEditSwiftFeeComponent,
    DialogEditSumFileConditionComponent,
    DialogSaveSumFileConditionComponent,
    DialogSaveSmartFeeComponent,
    DialogSaveSwiftFeeComponent,
    DialogOmDocTypeComponent,
    DialogOmPaymentMethodComponent,
    DialogConfirmDeleteMasterComponent,
    DialogConfirmComponent,
    DialogEditJobSchedComponent,
    DialogOmLogComponent,
    DialogConfirmStatusUsabilityComponent,
    DialogPaymentRunErrorDetailComponent,
    DialogReversePaymentComponent,
    DialogShowAutodocComponent,
    DialogReturnParameterGenFileComponent,
    TableReturnUpdateStatusComponent,
    DialogResultComponent,
    TableReturnReversePaymentComponent,
    TableReturnReverseInvoiceComponent,
    DialogArrangeColumnComponent,
    DialogSortColumnComponent,
    DialogFilterColumnComponent,
    DialogCriteriaComponent,
    DialogReturnLogComponent,
    DialogTaxFeeComponent,
    DialogOmSearchDetailComponent,
    DialogOmVendorComponent,
    DialogOmPaymentCenterComponent,
    DialogOmSpecialTypeComponent,
    DialogReversePaymentAllComponent,
    DialogMultipleSelectDocumentComponent,
    DialogPreviewSelectGroupDocumentComponent,
    DialogUploadComponent,
    DialogConfirmChangeDocumentComponent,
    DialogAuthenticationConfirmComponent,
    DialogBatchTransactionPaymentNameComponent,
    DialogOmFiAreaComponent,
    DialogSearchJobParameterComponent,
    DialogPoItemDetailComponent,
    DialogPoHeaderDetailComponent,
    DialogPoTaxComponent,
    DialogPoAdvancePaymentComponent,
    DialogBankAccountDetailComponent,
    DialogPoChangeHistoryComponent,
    DialogPoHistoryComponent,
    DialogWarningComponent,
    DialogUploadChangeDocumentComponent,
    DialogGenerateFileResultComponent,
    DialogVendorForPaymentComponent,
    DialogOmDocumentNoComponent,
    DialogOmHeaderReferenceComponent,
    DialogOmInputReasonUnapprovedComponent,
    DialogShowDocumentReferenceComponent,
    DialogShowErrorOmComponent,
    DialogDisplayArrangeColumnComponent,
    DialogSaveArrangeColumnComponent,
    DialogUpdateArrangeColumnComponent,
    DeleteButtonComponent,
  ],
})
export class SharedModule {}
