{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  packages = with pkgs; [
    scala_3
    scala-cli
    git
    p7zip
    ffmpeg
    wineWowPackages.stable
  ];
}
