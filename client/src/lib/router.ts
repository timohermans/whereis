import { RootRoute, Route, Router, redirect } from "@tanstack/react-router";
import App from "../App";
import Index from "../pages/Index";
import Login from "../pages/Login";
import { User, userKey } from "./types";
import { z } from "zod";
import Register from "../pages/Register";
import NotFound from "../pages/NotFound";

const rootRoute = new RootRoute({
  component: App,
});

async function enforceNotAuthenticated() {
  const user = getUser();
  if (user) {
    throw redirect({
      to: "/",
    });
  }
}

async function enforceAuthenticated() {
  const user = getUser();
  if (!user) {
    throw redirect({
      to: "/log-in",
      search: {
        redirect: router.state.location.href, // to force redirect on login again
      },
    });
  }
}

function getUser(): User | null {
  const userJson = localStorage.getItem(userKey);
  if (!userJson) return null;

  const user = JSON.parse(userJson) as User;

  if (!user || !user.refreshToken) return null;
  return user;
}

// =====
// ROUTES HERE
// =====

// /
const indexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "/",
  component: Index,
  beforeLoad: enforceAuthenticated,
});

// /log-in
const logInSearchSchema = z.object({
  redirect: z.string().optional(),
});
export const loginRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "log-in",
  component: Login,
  validateSearch: logInSearchSchema,
  beforeLoad: enforceNotAuthenticated,
});

// /register
export const registerRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "register",
  component: Register,
  beforeLoad: enforceNotAuthenticated,
});

const notFoundRoute = new Route({
  getParentRoute: () => rootRoute,
  component: NotFound,
  path: "$",
});

const routeTree = rootRoute.addChildren([
  indexRoute,
  loginRoute,
  registerRoute,
  notFoundRoute,
]);

export const router = new Router({ routeTree });

declare module "@tanstack/react-router" {
  interface Register {
    // This infers the type of our router and registers it across your entire project
    router: typeof router;
  }
}
