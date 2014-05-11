//
//  SettingsNavigationController.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "SettingsNavigationController.h"
#import "SettingsTableViewController.h"

@interface SettingsNavigationController ()

@end

@implementation SettingsNavigationController

- (id)init
{
    SettingsTableViewController * settingsTableViewController = [[SettingsTableViewController alloc] init];
    self = [super initWithRootViewController:settingsTableViewController];
    if (self) {
        self.tabBarItem.title = @"Settings";
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
