(function (window) {
  window.env = window.env || {};

  // Environment variables
  window['env']['apiUrl'] = 'http://localhost:8080/api';
  // window['env']['portal'] = 'https://portal-dev.in.gfmis.go.th/';
  // window['env']['callUserProfile'] = 'https://portal-dev.in.gfmis.go.th/iam-api/getUserProfile';
  // window['env']['callUserMatrix'] = 'https://portal-dev.in.gfmis.go.th/iam-api/getUserPrivileges';
  // window['env']['callUserAuthCode'] =
  //   'https://portal-dev.in.gfmis.go.th/iam-api/getTokenByAuthCode';
  window['env']['portal'] = 'https://portal-qas.gfmis.go.th/';
  window['env']['callUserProfile'] = 'https://portal-qas.gfmis.go.th/iam-api/getUserProfile';
  window['env']['callUserMatrix'] = 'https://portal-qas.gfmis.go.th/iam-api/getUserPrivileges';
  window['env']['callUserAuthCode'] = 'https://portal-qas.gfmis.go.th/iam-api/getTokenByAuthCode';
  // window['env']['portal'] = 'https://portal.gfmis.go.th/';
  // window['env']['callUserProfile'] = 'https://portal.gfmis.go.th/iam-api/getUserProfile';
  // window['env']['callUserMatrix'] = 'https://portal.gfmis.go.th/iam-api/getUserPrivileges';
  // window['env']['callUserAuthCode'] = 'https://portal.gfmis.go.th/iam-api/getTokenByAuthCode';
  // window['env']['portal'] = 'https://portal-trn.gfmis.go.th/';
  // window['env']['callUserProfile'] = 'https://portal-trn.gfmis.go.th/iam-api/getUserProfile';
  // window['env']['callUserMatrix'] = 'https://portal-trn.gfmis.go.th/iam-api/getUserPrivileges';
  // window['env']['callUserAuthCode'] = 'https://portal-trn.gfmis.go.th/iam-api/getTokenByAuthCode';
  // window['env']['portal'] = 'https://portal-ydev.gfmis.go.th/';
  // window['env']['callUserProfile'] = 'https://portal-ydev.gfmis.go.th/iam-api/getUserProfile';
  // window['env']['callUserMatrix'] = 'https://portal-ydev.gfmis.go.th/iam-api/getUserPrivileges';
  // window['env']['callUserAuthCode'] = 'https://portal-ydev.gfmis.go.th/iam-api/getTokenByAuthCode';
  // window['env']['portal'] = 'https://portal-pentest.gfmis.go.th/';
  // window['env']['callUserProfile'] = 'https://portal-pentest.gfmis.go.th/iam-api/getUserProfile';
  // window['env']['callUserMatrix'] = 'https://portal-pentest.gfmis.go.th/iam-api/getUserPrivileges';
  // window['env']['callUserAuthCode'] = 'https://portal-pentest.gfmis.go.th/iam-api/getTokenByAuthCode';
  window['env']['debug'] = '';
  window['env']['sftp'] = 'sftp://payment@10.220.56.100:8822';
  window['env']['path'] = './INCOMING/YDEV';
})(this);
