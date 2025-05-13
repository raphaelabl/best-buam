export const environment = {
  production: true,
  API_URL: "https://order.ffbruckmuehl.at/api/",
  WS_URL: "ws://order.ffbruckmuehl.at/api/",
  keycloak: {
    issuer: 'https://order.ffbruckmuehl.at/auth/',
    realm: 'bestbuam',
    clientId: 'bestbuam-frontend'
  }
};
