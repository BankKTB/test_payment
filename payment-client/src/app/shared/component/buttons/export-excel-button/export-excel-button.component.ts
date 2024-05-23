import { Component, Input } from '@angular/core';
import { UtilsService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import * as FileSaver from 'file-saver';
import { ExportFileButton } from '@shared/component/buttons/abstracts/export-file-button';

@Component({
  selector: 'app-export-excel-button',
  templateUrl: './export-excel-button.component.html',
  styleUrls: ['./export-excel-button.component.scss'],
})
export class ExportExcelButtonComponent extends ExportFileButton {
  @Input() onlyIcon: boolean = false;

  constructor(private utilsService: UtilsService, private utils: Utils) {
    super(utils);
  }

  exportReportExcel() {
    console.log(JSON.stringify(this.parseCriteria()));

    if (this.pathServiceExport) {
      this.utilsService
        .exportExcelHeaderData(this.parseCriteria(), this.pathServiceExport)
        .then((result) => {
          if (result.status === 200) {
            if (result.file) {
              FileSaver.saveAs(
                this.utils.convertBase64ToByteExcel(result.file),
                this.fileNameExport + '.xlsx'
              );
            } else {
              alert('ไม่พบไฟล์');
            }
          }
        });
    }
  }
}
