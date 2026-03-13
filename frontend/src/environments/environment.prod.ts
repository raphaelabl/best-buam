export const environment = {
  production: true,
  API_URL: "http://100.94.234.78:8080/",
  WS_URL: "ws://100.94.234.78:8080/",
  keycloak: {
    issuer: 'http://100.94.234.78:8081/auth/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
