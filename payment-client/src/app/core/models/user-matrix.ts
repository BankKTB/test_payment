import { Privilege } from '@core/models/privilege';

export interface UserMatrix {
  systemCode: string;
  userProfileCode: string;
  roleCode: string;
  roleName: string;
  privilege: Privilege;
}
