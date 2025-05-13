export const environment = {
  production: false,
  API_URL: "https://order.ffbruckmuehl.at/auth/",
  WS_URL: "ws://localhost:8080/",
  keycloak: {
    issuer: 'https://order.ffbruckmuehl.at/auth/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
