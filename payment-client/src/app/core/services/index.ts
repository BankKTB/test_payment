import { AuthGuardService } from '@core/services/authorize/auth-guard.service';
import { AuthorizeService } from '@core/services/authorize/authorize.service';
import { EncryptDecryptService } from '@core/services/encrypt-decrypt.service';
import { SidebarService } from '@core/services/sidebar.service';
import { WebInfoService } from '@core/services/web-info.service';
import { LocalStorageService } from '@core/services/local-storage.service';
import { LoaderService } from '@core/services/loader/loader.service';
import { MasterService } from '@core/services/master/master.service';
import { ParentPathService } from '@core/services/parent-path/parent-path.service';
import { UtilsService } from '@core/services/utils/utils.service';

// tslint:disable-next-line:max-line-length
export const services: any[] = [
  AuthGuardService,
  AuthorizeService,
  EncryptDecryptService,
  LocalStorageService,
  SidebarService,
  WebInfoService,
  LoaderService,
  MasterService,
  ParentPathService,
  UtilsService,
];

export * from './authorize/auth-guard.service';
export * from './authorize/authorize.service';
export * from './encrypt-decrypt.service';
export * from './local-storage.service';
export * from './sidebar.service';
export * from './web-info.service';
export * from './loader/loader.service';
export * from './master/master.service';
export * from './parent-path/parent-path.service';
export * from './utils/utils.service';
