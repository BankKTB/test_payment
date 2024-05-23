export const environment = {
  production: true,
  apiUrl: window['env']['apiUrl'] || 'default',
  debug: window['env']['debug'] || false,
  portal: window['env']['portal'] || 'default',
  callUserProfile: window['env']['callUserProfile'] || 'default',
  callUserMatrix: window['env']['callUserMatrix'] || 'default',
  callUserAuthCode: window['env']['callUserAuthCode'] || 'default',
  sftp: window['env']['sftp'] || 'default',
  path: window['env']['path'] || 'default',
};
