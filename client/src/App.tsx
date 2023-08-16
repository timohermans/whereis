import {
  ChangeEvent,
  FormEvent,
  useState,
} from "react";
import { useLocalStorage } from "./hooks";

type User = {
  token: string;
  refreshToken: string;
  email: string;
  roles: string[];
  tokenType: string;
};

function App() {
  const [form, setForm] = useState({});
  const [message, setMessage] = useState("");
  const [user, setUser] = useLocalStorage<User>("user", null);

  async function handleSumbit(event: FormEvent) {
    event.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/sign-in", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(form),
    });

    if (response.status === 403) {
      setMessage(
        "Invalid username or password 😔. Can you check and try again?",
      );
    } else if (response.status !== 200) {
      setMessage("Something unexpected went wrong. Oh noes 😱");
    } else {
      const user = await response.json();
      setUser(user);
    }
  }

  function handleChange(event: ChangeEvent<HTMLInputElement>) {
    const key = event.target.name;
    const value = event.target.value;
    setForm({
      ...form,
      [key]: value,
    });
  }

  return (
    <div className="">
      {user != null ? <p>Logged in :)</p> : <p>not logged in :(</p>}
      {message && <p>{message}</p>}
      {user == null && (
        <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
          <div className="sm:mx-auto sm:w-full sm:max-w-sm">
            <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
              Sign in to your account
            </h2>
          </div>

          <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
            <form className="space-y-6" onSubmit={handleSumbit}>
              <div>
                <label
                  htmlFor="email"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  Email address
                </label>
                <div className="mt-2">
                  <input
                    id="email"
                    name="email"
                    type="email"
                    onChange={handleChange}
                    required
                    className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  />
                </div>
              </div>

              <div>
                <div className="flex items-center justify-between">
                  <label
                    htmlFor="password"
                    className="block text-sm font-medium leading-6 text-gray-900"
                  >
                    Password
                  </label>
                </div>
                <div className="mt-2">
                  <input
                    id="password"
                    name="password"
                    type="password"
                    required
                    className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                    onChange={handleChange}
                  />
                </div>
              </div>

              <div>
                <button
                  type="submit"
                  className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                  Sign in
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

function isLoggedIn() {
  const token = localStorage.getItem("token");

  return !!token;
}

export default App;
