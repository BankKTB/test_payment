import { TemplateRef } from '@angular/core';

export interface ColumnData {
  key: string;
  sequence?: string;
  sortType?: string;
  isGroup: string;
  type: string; //CURRENCY, PERCENT , NUMBER, TEXT, YEAR
  typeInput?: string; //'DATE', 'STRING', 'NUMBER'
  width?: string;
  isLineBrake?: boolean;
  caption?: string; //*Tao 2021-03-02 caption translate
  template?: () => TemplateRef<unknown>;
  format?: (item, row?) => string;
  classes?: () => string | string;
  headerClasses?: () => string | string;
  translateManual?: string;
  isFreezedColumn?: boolean;
  disableSearchFilter?: boolean;
  isVisible?: boolean;
}
