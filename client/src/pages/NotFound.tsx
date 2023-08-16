import { Link } from "@tanstack/react-router";
import LostImage from "../assets/images/lost-items.png";

export default function NotFound() {
  return (
    <section className="flex flex-col items-center justify-center gap-8 px-6 py-12 lg:px-8">
      <h1 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
        Where are we?
      </h1>
      <p>
        <img className="mx-auto w-1/2" src={LostImage} alt="lost pic" />
      </p>
      <p>
        <Link
          className="text-indigo-600 hover:text-indigo-500 hover:underline font-bold"
          to="/"
        >
          🛫 let's return home
        </Link>
      </p>
    </section>
  );
}
