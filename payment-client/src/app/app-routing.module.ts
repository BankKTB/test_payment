import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InitFromIamComponent } from '@pages/init-from-iam/init-from-iam.component';
import { AuthGuardService } from '@core/services';
import { HolidayComponent } from '@pages/holiday/holiday.component';

const routes: Routes = [
  { path: '', component: InitFromIamComponent },
  {
    path: 'menu',
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/menu/menu.module').then((m) => m.MenuModule),
  },
  // {
  //   path: 'om',
  //   loadChildren: () => import('./pages/om/om.module').then((m) => m.OmModule),
  // },
  {
    path: 'omCgd',
    data: { auth: 'AP01' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/om-cgd/om-cgd.module').then((m) => m.OmCgdModule),
  },
  {
    path: 'omPto',
    data: { auth: 'AP02' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/om-pto/om-pto.module').then((m) => m.OmPtoModule),
  },
  {
    path: 'omPtoH',
    data: { auth: 'AP03' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/om-pto-h/om-pto-h.module').then((m) => m.OmPtoHModule),
  },
  {
    path: 'omLog',
    data: { auth: 'AP04' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/om-log/om-log.module').then((m) => m.OmLogModule),
  },
  {
    path: 'om-result',
    data: { auth: 'AP05' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/om-result/om-result.module').then((m) => m.OmResultModule),
  },
  {
    path: 'om-result-e',
    data: { auth: 'AP06' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/om-result-e/om-result-e.module').then((m) => m.OmResultEModule),
  },
  {
    path: 'om-result-summary',
    data: { auth: 'AP07' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/om-result-summary/om-result-summary.module').then(
        (m) => m.OmResultSummaryModule
      ),
  },
  {
    path: 'om-result-e-summary',
    data: { auth: 'AP08' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/om-result-e-summary/om-result-e-summary.module').then(
        (m) => m.OmResultESummaryModule
      ),
  },
  {
    path: 'companyPayee',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/company-payee/company-payee.module').then((m) => m.CompanyPayeeModule),
  },
  {
    path: 'companyPayeeBankAccountNoConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import(
        './pages/company-payee-bank-account-no-config/company-payee-bank-account-no-config.module'
      ).then((m) => m.CompanyPayeeBankAccountNoConfigModule),
  },
  {
    path: 'companyPayeeBankAccountNoDetailConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import(
        './pages/company-payee-bank-account-no-detail-config/company-payee-bank-account-no-detail-config.module'
      ).then((m) => m.CompanyPayeeBankAccountNoDetailConfigModule),
  },
  {
    path: 'companyPayeeHouseBankKeyConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import(
        './pages/company-payee-house-bank-key-config/company-payee-house-bank-key-config.module'
      ).then((m) => m.CompanyPayeeHouseBankKeyConfigModule),
  },
  {
    path: 'companyPayeeHouseBankKeyDetailConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import(
        './pages/company-payee-house-bank-key-detail-config/company-payee-house-bank-key-detail-config.module'
      ).then((m) => m.CompanyPayeeHouseBankKeyDetailConfigModule),
  },
  {
    path: 'companyPaying',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/company-paying/company-paying.module').then((m) => m.CompanyPayingModule),
  },
  {
    path: 'companyPayingBankConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/company-paying-bank/company-paying-bank-config.module').then(
        (m) => m.CompanyPayingBankConfigModule
      ),
  },
  {
    path: 'companyPayingPayMethod',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/company-paying-pay-method/company-paying-pay-method.module').then(
        (m) => m.CompanyPayingPayMethodModule
      ),
  },
  {
    path: 'companyPayingPayMethodConfig',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import(
        './pages/company-paying-pay-method-config/company-paying-pay-method-config.module'
      ).then((m) => m.CompanyPayingPayMethodConfigModule),
  },
  {
    path: 'payMethod',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/pay-method/pay-method.module').then((m) => m.PayMethodModule),
  },
  {
    path: 'payment',
    data: { auth: 'PM01' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/payment/payment.module').then((m) => m.PaymentModule),
  },
  {
    path: 'reverse/payment',
    data: { auth: 'KJ01' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/payment-reverse/payment-reverse.module').then((m) => m.PaymentReverseModule),
  },
  {
    path: 'reverse/invoice',
    data: { auth: 'KJ02' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/invoice-reverse/invoice-reverse.module').then((m) => m.InvoiceReverseModule),
  },
  // {
  //   path: 'report',
  //   // canActivate: [AuthGuardService],
  //   loadChildren: () => import('./shared/pages/report/report.module').then((m) => m.ReportModule),
  // },
  // {
  //   path: 'reportOm',
  //   // canActivate: [AuthGuardService],
  //   loadChildren: () =>
  //     import('./shared/pages/report-om/report-om.module').then((m) => m.ReportOmModule),
  // },
  {
    path: 'log',
    // canActivate: [AuthGuardService],
    loadChildren: () => import('./shared/pages/log/log.module').then((m) => m.LogModule),
  },
  {
    path: 'display',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./shared/pages/display/display.module').then((m) => m.DisplayModule),
  },
  {
    path: 'generate',
    data: { auth: 'CF01' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/generate-file/generate-file.module').then((m) => m.GenerateFileModule),
  },
  {
    path: 'generate-pac',
    data: { auth: 'CF02' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/generate-file-pac/generate-file-pac.module').then(
        (m) => m.GenerateFilePacModule
      ),
  },
  {
    path: 'generateReport',
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/generate-file-report/generate-file-report.module').then(
        (m) => m.GenerateFileReportModule
      ),
  },
  {
    path: 'generate-result',
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/generate-file-result/generate-file-result.module').then(
        (m) => m.GenerateFileResultModule
      ),
  },
  {
    path: 'swiftFee',
    data: { auth: 'SS05' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/swift-fee/swift-fee.module').then((m) => m.SwiftFeeModule),
  },
  {
    path: 'selectGroupDocument',
    // data: { auth: 'SS05' },
    // canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/select-group-document/select-group-document.module').then(
        (m) => m.SelectGroupDocumentModule
      ),
  },
  {
    path: 'smartFee',
    data: { auth: 'SS04' },
    canActivate: [AuthGuardService],
    loadChildren: () => import('./pages/smart-fee/smart-fee.module').then((m) => m.SmartFeeModule),
  },
  {
    path: 'sumFileCondition',
    data: { auth: 'SS06' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/sum-file-condition/sum-file-condition.module').then(
        (m) => m.SumFileConditionModule
      ),
  },
  {
    path: 'view-swiftFee',
    data: { auth: 'SS08' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/view-swift-fee/view-swift-fee.module').then((m) => m.ViewSwiftFeeModule),
  },
  {
    path: 'view-smartFee',
    data: { auth: 'SS07' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/view-smart-fee/view-smart-fee.module').then((m) => m.ViewSmartFeeModule),
  },
  {
    path: 'view-sumFileCondition',
    data: { auth: 'SS09' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/view-sum-file-condition/view-sum-file-condition.module').then(
        (m) => m.ViewSumFileConditionModule
      ),
  },
  {
    path: 'detail-fi-kb',
    loadChildren: () =>
      import('./shared/pages/detail-fi-kb/detail-fi-kb.module').then((m) => m.DetailFiKbModule),
  },
  {
    path: 'detail-po',
    loadChildren: () =>
      import('./shared/pages/detail-po/detail-po.module').then((m) => m.DetailPoModule),
  },
  {
    path: 'detail-sng',
    loadChildren: () =>
      import('./shared/pages/detail-sng/detail-sng.module').then((m) => m.DetailSngModule),
  },
  {
    path: 'detail-po4',
    loadChildren: () =>
      import('./shared/pages/detail-po4/detail-po4.module').then((m) => m.DetailPo4Module),
  },
  {
    path: 'detail-fi-ju',
    loadChildren: () =>
      import('./shared/pages/detail-fi-ju/detail-fi-ju.module').then((m) => m.DetailFiJuModule),
  },
  {
    path: 'jobSched',
    loadChildren: () => import('./pages/job-sched/job-sched.module').then((m) => m.JobSchedModule),
  },
  {
    path: 'proposal-detail',
    loadChildren: () =>
      import('./pages/proposal-detail/proposal-detail.module').then((m) => m.ProposalDetailModule),
  },
  {
    path: 'payment-run-error',
    loadChildren: () =>
      import('./pages/payment-run-error/payment-run-error.module').then(
        (m) => m.PaymentRunErrorModule
      ),
  },
  {
    path: 'return',
    loadChildren: () => import('./pages/return/return.module').then((m) => m.ReturnModule),
  },
  {
    path: 'generate-ju',
    loadChildren: () =>
      import('./pages/generate-document-ju/generate-document-ju.module').then(
        (m) => m.GenerateDocumentJuModule
      ),
  },
  {
    path: 'change-document',
    data: { auth: 'CHLITM' },
    canActivate: [AuthGuardService],
    loadChildren: () =>
      import('./pages/change-document/change-document.module').then((m) => m.ChangeDocumentModule),
  },
  {
    path: 'batch-job',
    loadChildren: () => import('./pages/batch-job/batch-job.module').then((m) => m.BatchJobModule),
  },
  {
    path: 'holiday',
    loadChildren: () => import('./pages/holiday/holiday.module').then((m) => m.HolidayModule),
  },
  {
    path: 'proposal-tr1',
    loadChildren: () =>
      import('./pages/proposal-tr1/proposal-tr1.module').then((m) => m.ProposalTr1Module),
  },
  {
    path: 'report-duplicate-pay',
    loadChildren: () =>
      import('./pages/report-duplicate-pay/report-duplicate-pay.module').then(
        (m) => m.ReportDuplicatePayModule
      ),
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      onSameUrlNavigation: 'reload',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
