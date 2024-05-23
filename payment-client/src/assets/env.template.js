(function (window) {
  window.env = window.env || {};

  // Environment variables
  window['env']['apiUrl'] = '${API_URL}';
  window['env']['portal'] = '${PORTAL_URL}';
  window['env']['callUserProfile'] = '${PORTALAPI_URL}';
  window['env']['callUserMatrix'] = '${PORTALMATRIX_URL}';
  window['env']['callUserAuthCode'] = '${PORTALAUTH_URL}';
  window['env']['debug'] = '${DEBUG}';
  window['env']['sftp'] = '${SFTP_URL}';
  window['env']['path'] = '${PATH_URI}';
})(this);
