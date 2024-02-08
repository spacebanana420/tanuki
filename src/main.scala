package tanuki

import tanuki.tui.*
import tanuki.config.*

@main def main() =
  if !configExists() then
    createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()

