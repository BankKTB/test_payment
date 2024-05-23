import { Component, Input } from '@angular/core';
import { UtilsService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import * as FileSaver from 'file-saver';
import { ExportFileButton } from '@shared/component/buttons/abstracts/export-file-button';

@Component({
  selector: 'app-export-pdf-button',
  templateUrl: './export-pdf-button.component.html',
  styleUrls: ['./export-pdf-button.component.scss'],
})
export class ExportPdfButtonComponent extends ExportFileButton {
  @Input() onlyIcon: boolean = false;
  constructor(private utilsService: UtilsService, private utils: Utils) {
    super(utils);
  }

  downloadFile() {
    if (this.pathServiceExport) {
      this.utilsService
        .exportExcelHeaderData(this.parseCriteria(), this.pathServiceExport)
        .then((result) => {
          if (result.status === 200) {
            if (result.file) {
              let fileName = this.fileNameExport;
              if (!this.setFileNameManual) {
                fileName = result.content.optionals.fileName;
              }
              FileSaver.saveAs(this.utils.convertBase64ToBytePdf(result.file), fileName);
            } else {
              alert('ไม่พบไฟล์');
            }
          } else {
            alert(' ไม่สามารถดาวโหลดไฟล์ได้ กรุณาลองใหม่อีกครั้ง ');
          }
        });
    }
  }

  exportReportPDF() {
    if (this.form === undefined) {
      this.downloadFile();
    } else {
      this.form.updateValueAndValidity();
      if (this.form.valid) {
        this.downloadFile();
      } else {
        this.onValidate.emit();
      }
    }
  }
}
