#include <stdio.h>
#include <string.h>

#include "shell.h"

#include "led.h"

int echo(int argc, char **argv)
{
    /* ... */
    (void)argc;
    (void)argv;

    return 0;
}

int toggleLed(int argc, char **argv) {
    LED0_TOGGLE;
    printf("LED is toggled\n");
    printf("%d %s\n", argc, argv[0]);
    return 0;
}

static const shell_command_t commands[] = {
    { "led", "Toggle LED", toggleLed },
    { NULL, NULL, NULL }
};

int main(void)
{
    puts("This is Task-02");

    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(commands, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
