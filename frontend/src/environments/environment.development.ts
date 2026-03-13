export const environment = {
  production: true,
  API_URL: "http://localhost:8080/",
  WS_URL: "ws://localhost:8080/",
  keycloak: {
    issuer: 'http://localhost:8081/auth/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
