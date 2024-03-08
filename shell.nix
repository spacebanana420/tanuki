{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  packages = with pkgs; [
    #Base packages
      scala_3
      scala-cli
      git
      p7zip
    #Runtime dependencies
      ffmpeg
      wineWowPackages.stable
      steam-run
    #Native compilation
      graalvm-ce
      gcc
      glibc
      zlib
  ];
}
