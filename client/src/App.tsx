import { useLocalStorage } from "./lib/hooks";
import { Outlet } from "@tanstack/react-router";
import { User } from "./lib/types";

function App() {
  const [user] = useLocalStorage<User>("user", null);

  return (
    <div className="">
      {user != null ? <p>Logged in :)</p> : <p>not logged in :(</p>}
      <Outlet />
    </div>
  );
}

export default App;
