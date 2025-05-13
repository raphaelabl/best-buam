export const environment = {
  production: false,
  API_URL: "http://localhost:8080/",
  WS_URL: "ws://localhost:8080",
  keycloak: {
    issuer: 'http://localhost:8090/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
