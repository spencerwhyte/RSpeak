//
//  CentralTabBarController.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "CentralTabBarController.h"
#import "OutboxNavigationViewController.h"
#import "InboxNavigationViewController.h"
#import "MagnetModeNavigationController.h"
#import "SettingsNavigationController.h"

@interface CentralTabBarController ()

@end

@implementation CentralTabBarController

- (id)init
{
    self = [super init];
    if (self) {
        
        OutboxNavigationViewController * outbox = [[OutboxNavigationViewController alloc]init];
        
        InboxNavigationViewController * inbox = [[InboxNavigationViewController alloc] init];
        
        MagnetModeNavigationController * magnet = [[MagnetModeNavigationController alloc] init];
        
        SettingsNavigationController * settings = [[SettingsNavigationController alloc] init];
        
        self.viewControllers = [NSArray arrayWithObjects: outbox, inbox, magnet, settings, nil];
        
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
