import { Permission } from '@core/models/permission';

export interface Screen {
  formId: string;
  page: string;
  pageWeb: string;
  pageDescritpion: string;
  permission: Permission[];
}
