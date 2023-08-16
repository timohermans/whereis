import { useLocalStorage } from "./lib/hooks";
import { Outlet } from "@tanstack/react-router";
import { User } from "./lib/types";
import { router } from "./lib/router";
import BrandImage from "./assets/images/teacher.png";

function App() {
  const [user, setUser] = useLocalStorage<User>("user", null);

  function logOut() {
    setUser(null);
    router.navigate({ to: "/log-in" });
  }

  return (
    <div className="flex min-h-screen flex-col gap-6">
      {user && (
        <header className="flex h-16 items-center justify-between px-6 lg:px-8">
          <div>
            <img src={BrandImage} alt="brand" className="h-16" />
          </div>
          <div className="flex items-center gap-4">
            <span>Hello, {user.email}</span>
            <button
              type="button"
              className="font-bold text-indigo-700 hover:underline"
              onClick={logOut}
            >
              Log out
            </button>
          </div>
        </header>
      )}
      <main className="flex-1 px-6 lg:px-8">
        <Outlet />
      </main>
    </div>
  );
}

export default App;
